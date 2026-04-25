<?php

namespace Services;

use Repositories\UserCategoryRepo;
use Repositories\ExercisePerformanceRepo;
use Repositories\UserPriorityRepo;
use Models\UserCategoryRank;
use Models\UserPriority;

class UserCategoryService
{
    private UserCategoryRepo $categoryRepo;
    private ExercisePerformanceRepo $exercisePerformanceRepo;
    private UserPriorityRepo $priorityRepo;
    
    public function __construct()
    {
        $this->categoryRepo = new UserCategoryRepo();
        $this->exercisePerformanceRepo = new ExercisePerformanceRepo();
        $this->priorityRepo = new UserPriorityRepo();
    }
    
    /**
     * Calculate user category rank (saves priorities automatically)
     */
    public function calculateUserCategoryRank(
    int $userId, 
    string $movementType,
    string $firstPriority,
    string $secondPriority,
    string $thirdPriority,
    string $fourthPriority
): array {
    
    error_log("=== calculateUserCategoryRank CALLED ===");
    error_log("userId: $userId");
    error_log("movementType RAW: '$movementType'");
    
    $normalizedType = ucfirst(strtolower(trim($movementType)));
    error_log("movementType NORMALIZED: '$normalizedType'");
    
    $priority = new UserPriority();
    $priority->setUserId($userId);
    $priority->setMovementType($normalizedType);
    $priority->setFirstPriority($firstPriority);
    $priority->setSecondPriority($secondPriority);
    $priority->setThirdPriority($thirdPriority);
    $priority->setFourthPriority($fourthPriority);
    
    error_log("About to save priority to DB...");
    $this->priorityRepo->save($priority);
    error_log("Priority saved successfully");
    
    error_log("About to calculateWithPriorities...");
    $result = $this->calculateWithPriorities(
        $userId, $normalizedType, $firstPriority, $secondPriority, $thirdPriority, $fourthPriority
    );
    error_log("calculateWithPriorities result: " . json_encode($result));
    return $result;
}
    
    /**
     * Get user category rank (returns null if not found)
     */
    public function getUserCategoryRank(int $userId, string $movementType): ?array
    {
        $normalizedMovementType = ucfirst(strtolower(trim($movementType)));
        $categoryRank = $this->categoryRepo->getByUserId($userId, $normalizedMovementType);
        
        if (!$categoryRank) {
            return null;
        }
        
        return [
            'movementType' => $categoryRank->getMovementType(),
            'categoryScore' => $categoryRank->getCategoryScore(),
            'categoryRank' => $categoryRank->getCategoryRank(),
            'calculatedAt' => $categoryRank->getCalculatedAt()
        ];
    }
    
    /**
     * Update user category rank (uses saved priorities if available)
     */
    public function updateUserCategoryRank(
        int $userId,
        string $movementType,
        ?string $firstPriority = null,
        ?string $secondPriority = null,
        ?string $thirdPriority = null,
        ?string $fourthPriority = null
    ): array {
        
        $normalizedMovementType = ucfirst(strtolower(trim($movementType)));
        
        // If priorities not provided, load saved ones
        if ($firstPriority === null) {
            $savedPriority = $this->priorityRepo->getByUserAndMovement($userId, $normalizedMovementType);
            if ($savedPriority) {
                $firstPriority = $savedPriority->getFirstPriority();
                $secondPriority = $savedPriority->getSecondPriority();
                $thirdPriority = $savedPriority->getThirdPriority();
                $fourthPriority = $savedPriority->getFourthPriority();
            } else {
                // Default priorities if nothing saved
                $firstPriority = 'Compound';
                $secondPriority = 'Calisthenics';
                $thirdPriority = 'PrimaryIsolation';
                $fourthPriority = 'PureIsolation';
            }
        }
        
        return $this->calculateWithPriorities(
            $userId, $movementType, $firstPriority, $secondPriority, $thirdPriority, $fourthPriority
        );
    }
    
    /**
     * Core calculation logic
     */
    private function calculateWithPriorities(
        int $userId,
        string $movementType,
        string $firstPriority,
        string $secondPriority,
        string $thirdPriority,
        string $fourthPriority
    ): array {
        error_log("=== calculateWithPriorities CALLED ===");
    error_log("movementType received: '$movementType'");
    error_log("normalized for comparison: '" . strtolower(trim($movementType)) . "'");
        
        $exercises = [];
        $movementTypeKey = '';
        $normalizedMovementType = strtolower(trim($movementType));
        error_log("Will match: push=" . ($normalizedMovementType === 'push' ? 'YES' : 'NO') . 
              " pull=" . ($normalizedMovementType === 'pull' ? 'YES' : 'NO') . 
              " legs=" . ($normalizedMovementType === 'legs' ? 'YES' : 'NO'));
        
        if ($normalizedMovementType === 'pull') {
            $exercises = $this->exercisePerformanceRepo->getPullPerformance($userId);
            $movementTypeKey = 'Pull';
        } else if ($normalizedMovementType === 'push') {
            $exercises = $this->exercisePerformanceRepo->getPushPerformance($userId);
            $movementTypeKey = 'Push';
        } else if ($normalizedMovementType === 'legs') {
            $exercises = $this->exercisePerformanceRepo->getLegsPerformance($userId);
            $movementTypeKey = 'Legs';
        } else {
            throw new \InvalidArgumentException("Invalid movement type. Must be 'pull', 'push', or 'legs'");
        }
        
        $priorityWeights = [
            $firstPriority => 0.45,
            $secondPriority => 0.25,
            $thirdPriority => 0.15,
            $fourthPriority => 0.10
        ];
        
        $grouped = [];
        foreach ($exercises as $exercise) {
            $exerciseType = $exercise->getExerciseType();
            if (!isset($grouped[$exerciseType])) {
                $grouped[$exerciseType] = [];
            }
            $grouped[$exerciseType][] = $exercise;
        }
        
        $usedWeight = 0;
        foreach ($priorityWeights as $exerciseType => $weight) {
            if (isset($grouped[$exerciseType])) {
                $usedWeight += $weight;
            }
        }
        
        $score = 0.0;
        
        if ($usedWeight > 0) {
            foreach ($priorityWeights as $exerciseType => $weight) {
                if (!isset($grouped[$exerciseType])) {
                    continue;
                }
                $normalizedWeight = $weight / $usedWeight;
                $typeExercises = $grouped[$exerciseType];
                $weightPerExercise = $normalizedWeight / count($typeExercises);
                
                foreach ($typeExercises as $exercise) {
                    $score += $exercise->getRelativeStrength() * $weightPerExercise;
                }
            }
        }
        
        $categoryRank = $this->generateRank($score);
        
        $existingRank = $this->categoryRepo->getByUserId($userId, $movementTypeKey);
        
        if ($existingRank) {
            $existingRank->setCategoryScore($score);
            $existingRank->setCategoryRank($categoryRank);
            $existingRank->setCalculatedAt(date('Y-m-d H:i:s'));
            $this->categoryRepo->update($existingRank);
        } else {
            $userCategoryRank = new UserCategoryRank();
            $userCategoryRank->setUserId($userId);
            $userCategoryRank->setMovementType($movementTypeKey);
            $userCategoryRank->setCategoryScore($score);
            $userCategoryRank->setCategoryRank($categoryRank);
            $userCategoryRank->setCalculatedAt(date('Y-m-d H:i:s'));
            $this->categoryRepo->create($userCategoryRank);
        }
        
        return [
            'movementType' => $movementTypeKey,
            'categoryScore' => $score,
            'categoryRank' => $categoryRank,
            'calculatedAt' => date('Y-m-d H:i:s')
        ];
    }
    
    private function generateRank(float $score): string
    {
        if ($score < 0.6) return "BronzeI";
        if ($score >= 0.60 && $score < 0.7) return "SilverIII";
        if ($score >= 0.7 && $score < 0.8) return "SilverII";
        if ($score >= 0.8 && $score < 0.9) return "SilverI";
        if ($score >= 0.9 && $score < 1.0) return "GoldIII";
        if ($score >= 1.0 && $score < 1.1) return "GoldII";
        if ($score >= 1.1 && $score < 1.2) return "GoldI";
        if ($score >= 1.2 && $score < 1.3) return "PlatinumIII";
        if ($score >= 1.3 && $score < 1.4) return "PlatinumII";
        if ($score >= 1.4 && $score < 1.5) return "PlatinumI";
        if ($score >= 1.5 && $score < 1.6) return "DiamondIII";
        if ($score >= 1.6 && $score < 1.7) return "DiamondII";
        if ($score >= 1.7 && $score < 1.8) return "DiamondI";
        if ($score >= 1.8 && $score < 1.9) return "EliteIII";
        if ($score >= 1.9 && $score < 2.0) return "EliteII";
        return "EliteI";
    }
}