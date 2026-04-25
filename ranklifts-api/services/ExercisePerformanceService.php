<?php

namespace Services;

use Repositories\ExercisePerformanceRepo;
use Repositories\ExerciseRepo;
use Repositories\ProfileRepo;
use Models\UserExercisePerformance;

class ExercisePerformanceService
{
    private ExercisePerformanceRepo $performanceRepo;
    private ExerciseRepo $exerciseRepo;
    private ProfileRepo $profileRepo;
    
    public function __construct()
    {
        $this->performanceRepo = new ExercisePerformanceRepo();
        $this->exerciseRepo = new ExerciseRepo();
        $this->profileRepo = new ProfileRepo();
    }
    
    /**
     * Calculate exercise rank for a new performance
     */
    public function calculateExerciseRank(int $userId, string $exerciseName, float $addedWeight, int $reps): array
    {
        // Get exercise details
        $exercise = $this->exerciseRepo->getByExerciseName($exerciseName);
        if (!$exercise) {
            throw new \Exception("Exercise not found: " . $exerciseName);
        }
        
        // Get user profile for bodyweight
        $profile = $this->profileRepo->getByUserId($userId);
        if (!$profile) {
            throw new \Exception("Profile not found for user. Please complete your profile first.");
        }
        
        $estimated1RM = 0;
        $relativeStrength = 0;
        $rank = '';
        
        // Calculate based on exercise type
        $exerciseType = $exercise->getExerciseType();
        
        if ($exerciseType === 'Compound') {
            $estimated1RM = $addedWeight * (1 + ($reps / 30.0));
            $relativeStrength = round($estimated1RM / $profile->getBodyweight(), 1);
            $rank = $this->generateRankCompound($relativeStrength);
            
        } else if ($exerciseType === 'Calisthenics') {
            $estimated1RM = (($profile->getBodyweight() * $exercise->getMovementFactor()) + $addedWeight) * (1 + ($reps / 30.0));
            $relativeStrength = round($estimated1RM / $profile->getBodyweight(), 1);
            $rank = $this->generateRankCompound($relativeStrength);
            
        } else if ($exerciseType === 'PureIsolation') {
            $estimated1RM = $addedWeight * (1 + ($reps / 30.0));
            $referenceWeight = $profile->getBodyweight() * $exercise->getIsolationFactor();
            $relativeStrength = round($estimated1RM / $referenceWeight, 1);
            $rank = $this->generateRankPureIsolation($relativeStrength);
            
        } else if ($exerciseType === 'PrimaryIsolation') {
            $estimated1RM = $addedWeight * (1 + ($reps / 30.0));
            $referenceWeight = $profile->getBodyweight() * $exercise->getIsolationFactor();
            $relativeStrength = round($estimated1RM / $referenceWeight, 1);
            $rank = $this->generateRankPrimaryIsolation($relativeStrength);
            
        } else {
            throw new \Exception("Unknown exercise type: " . $exerciseType);
        }
        
        // Create performance record
        $performance = new UserExercisePerformance();
        $performance->setUserId($userId);
        $performance->setExerciseId($exercise->getExerciseId());
        $performance->setExerciseName($exerciseName);
        $performance->setExerciseType($exerciseType);
        $performance->setAddedWeight($addedWeight);
        $performance->setReps($reps);
        $performance->setEstimated1RM($estimated1RM);
        $performance->setRelativeStrength($relativeStrength);
        $performance->setExerciseRank($rank);
        $performance->setRecordedAt(date('Y-m-d H:i:s'));
        
        $this->performanceRepo->create($performance);
        
        return [
            'performanceId' => $performance->getPerformanceId(),
            'exerciseName' => $exerciseName,
            'addedWeight' => $addedWeight,
            'reps' => $reps,
            'estimated1RM' => $estimated1RM,
            'relativeStrength' => $relativeStrength,
            'exerciseRank' => $rank,
            'recordedAt' => $performance->getRecordedAt(),
            'exerciseType' => $exerciseType,
            'exerciseId' => $exercise->getExerciseId()
        ];
    }
    
    /**
     * Update exercise rank for existing performance
     */
    public function updateExerciseRank(int $userId, int $performanceId, string $exerciseName, float $addedWeight, int $reps): array
    {
        // Get existing performance
        $existingPerformance = $this->performanceRepo->getByPerformanceId($performanceId);
        if (!$existingPerformance) {
            throw new \Exception("Performance not found.");
        }
        
        // Verify ownership
        if ($existingPerformance->getUserId() !== $userId) {
            throw new \Exception("Access denied.");
        }
        
        // Get exercise details
        $exercise = $this->exerciseRepo->getByExerciseName($exerciseName);
        if (!$exercise) {
            throw new \Exception("Exercise not found: " . $exerciseName);
        }
        
        // Get user profile
        $profile = $this->profileRepo->getByUserId($userId);
        if (!$profile) {
            throw new \Exception("Profile not found.");
        }
        
        $estimated1RM = 0;
        $relativeStrength = 0;
        $rank = '';
        
        $exerciseType = $exercise->getExerciseType();
        
        if ($exerciseType === 'Compound') {
            $estimated1RM = $addedWeight * (1 + ($reps / 30.0));
            $relativeStrength = round($estimated1RM / $profile->getBodyweight(), 1);
            $rank = $this->generateRankCompound($relativeStrength);
            
        } else if ($exerciseType === 'Calisthenics') {
            $estimated1RM = (($profile->getBodyweight() * $exercise->getMovementFactor()) + $addedWeight) * (1 + ($reps / 30.0));
            $relativeStrength = round($estimated1RM / $profile->getBodyweight(), 1);
            $rank = $this->generateRankCompound($relativeStrength);
            
        } else if ($exerciseType === 'PureIsolation') {
            $estimated1RM = $addedWeight * (1 + ($reps / 30.0));
            $referenceWeight = $profile->getBodyweight() * $exercise->getIsolationFactor();
            $relativeStrength = round($estimated1RM / $referenceWeight, 1);
            $rank = $this->generateRankPureIsolation($relativeStrength);
            
        } else if ($exerciseType === 'PrimaryIsolation') {
            $estimated1RM = $addedWeight * (1 + ($reps / 30.0));
            $referenceWeight = $profile->getBodyweight() * $exercise->getIsolationFactor();
            $relativeStrength = round($estimated1RM / $referenceWeight, 1);
            $rank = $this->generateRankPrimaryIsolation($relativeStrength);
        }
        
        // Update performance
        $existingPerformance->setExerciseId($exercise->getExerciseId());
        $existingPerformance->setExerciseName($exerciseName);
        $existingPerformance->setExerciseType($exerciseType);
        $existingPerformance->setAddedWeight($addedWeight);
        $existingPerformance->setReps($reps);
        $existingPerformance->setEstimated1RM($estimated1RM);
        $existingPerformance->setRelativeStrength($relativeStrength);
        $existingPerformance->setExerciseRank($rank);
        $existingPerformance->setRecordedAt(date('Y-m-d H:i:s'));
        
        $this->performanceRepo->update($existingPerformance);
        
        return [
            'performanceId' => $existingPerformance->getPerformanceId(),
            'exerciseName' => $exerciseName,
            'addedWeight' => $addedWeight,
            'reps' => $reps,
            'estimated1RM' => $estimated1RM,
            'relativeStrength' => $relativeStrength,
            'exerciseRank' => $rank,
            'recordedAt' => $existingPerformance->getRecordedAt(),
            'exerciseType' => $exerciseType,
            'exerciseId' => $exercise->getExerciseId()
        ];
    }
    
    /**
     * Get exercise performance by ID
     */
    public function getExercisePerformanceById(int $userId, int $performanceId): array
    {
        $performance = $this->performanceRepo->getByPerformanceId($performanceId);
        
        if (!$performance || $performance->getUserId() !== $userId) {
            throw new \Exception("Exercise performance not found or access denied.");
        }
        
        return [
            'performanceId' => $performance->getPerformanceId(),
            'exerciseName' => $performance->getExerciseName(),
            'addedWeight' => $performance->getAddedWeight(),
            'reps' => $performance->getReps(),
            'estimated1RM' => $performance->getEstimated1RM(),
            'relativeStrength' => $performance->getRelativeStrength(),
            'exerciseRank' => $performance->getExerciseRank(),
            'recordedAt' => $performance->getRecordedAt(),
            'exerciseType' => $performance->getExerciseType(),
            'exerciseId' => $performance->getExerciseId()
        ];
    }
    
    /**
     * Delete exercise performance
     */
    public function deleteExercisePerformance(int $userId, int $performanceId): array
    {
        $performance = $this->performanceRepo->getByPerformanceId($performanceId);
        
        if (!$performance || $performance->getUserId() !== $userId) {
            throw new \Exception("Exercise performance not found or access denied.");
        }
        
        $response = [
            'performanceId' => $performance->getPerformanceId(),
            'exerciseName' => $performance->getExerciseName(),
            'addedWeight' => $performance->getAddedWeight(),
            'reps' => $performance->getReps(),
            'estimated1RM' => $performance->getEstimated1RM(),
            'relativeStrength' => $performance->getRelativeStrength(),
            'exerciseRank' => $performance->getExerciseRank(),
            'recordedAt' => $performance->getRecordedAt(),
            'exerciseType' => $performance->getExerciseType(),
            'exerciseId' => $performance->getExerciseId()
        ];
        
        $this->performanceRepo->delete($performanceId);
        
        return $response;
    }
    
    /**
     * Get user's pull performances
     */
    public function getUserPullPerformance(int $userId): array
    {
        $performances = $this->performanceRepo->getPullPerformance($userId);
        return array_map(function($p) {
            return $p->toArray();
        }, $performances);
    }
    
    /**
     * Get user's push performances
     */
    public function getUserPushPerformance(int $userId): array
    {
        $performances = $this->performanceRepo->getPushPerformance($userId);
        return array_map(function($p) {
            return $p->toArray();
        }, $performances);
    }
    
    /**
     * Get user's legs performances
     */
    public function getUserLegsPerformance(int $userId): array
    {
        $performances = $this->performanceRepo->getLegsPerformance($userId);
        return array_map(function($p) {
            return $p->toArray();
        }, $performances);
    }
    
    /**
     * Batch update exercises
     */
    public function updateExerciseBatch(int $userId, array $batchRequests): array
    {
        $results = [];
        
        // ExercisePerformanceService.php → updateExerciseBatch()
foreach ($batchRequests as $request) {
    try {
        // Use array_key_exists instead of isset — isset returns false for null values
        $hasPerformanceId = array_key_exists('performanceId', $request) 
                            && $request['performanceId'] !== null 
                            && $request['performanceId'] > 0;
        
        if ($hasPerformanceId) {
            $result = $this->updateExerciseRank(
                $userId,
                (int)$request['performanceId'],
                $request['exerciseName'],
                (float)$request['addedWeight'],
                (int)$request['reps']
            );
        } else {
            $result = $this->calculateExerciseRank(
                $userId,
                $request['exerciseName'],
                (float)$request['addedWeight'],
                (int)$request['reps']
            );
        }
        $results[] = $result;
    } catch (\Exception $e) {
        error_log("Batch error for {$request['exerciseName']}: " . $e->getMessage());
        // Currently silently skipping — add this so you can see failures:
        $results[] = ['error' => $e->getMessage(), 'exerciseName' => $request['exerciseName']];
    }
}
        
        return $results;
    }
    
    // ============ Rank Generation Methods ============
    
    private function generateRankCompound(float $relativeStrength): string
    {
        if ($relativeStrength < 0.6) return "BronzeI";
        if ($relativeStrength >= 0.6 && $relativeStrength < 0.7) return "SilverIII";
        if ($relativeStrength >= 0.7 && $relativeStrength < 0.8) return "SilverII";
        if ($relativeStrength >= 0.8 && $relativeStrength < 0.9) return "SilverI";
        if ($relativeStrength >= 0.9 && $relativeStrength < 1.0) return "GoldIII";
        if ($relativeStrength >= 1.0 && $relativeStrength < 1.1) return "GoldII";
        if ($relativeStrength >= 1.1 && $relativeStrength < 1.2) return "GoldI";
        if ($relativeStrength >= 1.2 && $relativeStrength < 1.3) return "PlatinumIII";
        if ($relativeStrength >= 1.3 && $relativeStrength < 1.4) return "PlatinumII";
        if ($relativeStrength >= 1.4 && $relativeStrength < 1.5) return "PlatinumI";
        if ($relativeStrength >= 1.5 && $relativeStrength < 1.6) return "DiamondIII";
        if ($relativeStrength >= 1.6 && $relativeStrength < 1.7) return "DiamondII";
        if ($relativeStrength >= 1.7 && $relativeStrength < 1.8) return "DiamondI";
        if ($relativeStrength >= 1.8 && $relativeStrength < 1.9) return "EliteIII";
        if ($relativeStrength >= 1.9 && $relativeStrength < 2.0) return "EliteII";
        return "EliteI";
    }
    
    private function generateRankPureIsolation(float $relativeStrength): string
    {
        if ($relativeStrength < 0.8) return "BronzeI";
        if ($relativeStrength >= 0.8 && $relativeStrength < 0.95) return "SilverIII";
        if ($relativeStrength >= 0.95 && $relativeStrength < 1.1) return "SilverII";
        if ($relativeStrength >= 1.1 && $relativeStrength < 1.25) return "SilverI";
        if ($relativeStrength >= 1.25 && $relativeStrength < 1.4) return "GoldIII";
        if ($relativeStrength >= 1.4 && $relativeStrength < 1.55) return "GoldII";
        if ($relativeStrength >= 1.55 && $relativeStrength < 1.7) return "GoldI";
        if ($relativeStrength >= 1.7 && $relativeStrength < 1.9) return "PlatinumIII";
        if ($relativeStrength >= 1.9 && $relativeStrength < 2.1) return "PlatinumII";
        if ($relativeStrength >= 2.1 && $relativeStrength < 2.3) return "PlatinumI";
        if ($relativeStrength >= 2.3 && $relativeStrength < 2.5) return "DiamondIII";
        if ($relativeStrength >= 2.5 && $relativeStrength < 2.7) return "DiamondII";
        if ($relativeStrength >= 2.7 && $relativeStrength < 2.9) return "DiamondI";
        if ($relativeStrength >= 2.9 && $relativeStrength < 3.1) return "EliteIII";
        if ($relativeStrength >= 3.1 && $relativeStrength < 3.3) return "EliteII";
        return "EliteI";
    }
    
    private function generateRankPrimaryIsolation(float $relativeStrength): string
    {
        if ($relativeStrength < 0.9) return "BronzeI";
        if ($relativeStrength >= 0.9 && $relativeStrength < 1.0) return "SilverIII";
        if ($relativeStrength >= 1.0 && $relativeStrength < 1.1) return "SilverII";
        if ($relativeStrength >= 1.1 && $relativeStrength < 1.2) return "SilverI";
        if ($relativeStrength >= 1.2 && $relativeStrength < 1.35) return "GoldIII";
        if ($relativeStrength >= 1.35 && $relativeStrength < 1.5) return "GoldII";
        if ($relativeStrength >= 1.5 && $relativeStrength < 1.65) return "GoldI";
        if ($relativeStrength >= 1.65 && $relativeStrength < 1.8) return "PlatinumIII";
        if ($relativeStrength >= 1.8 && $relativeStrength < 2.0) return "PlatinumII";
        if ($relativeStrength >= 2.0 && $relativeStrength < 2.2) return "PlatinumI";
        if ($relativeStrength >= 2.2 && $relativeStrength < 2.35) return "DiamondIII";
        if ($relativeStrength >= 2.35 && $relativeStrength < 2.5) return "DiamondII";
        if ($relativeStrength >= 2.5 && $relativeStrength < 2.6) return "DiamondI";
        if ($relativeStrength >= 2.6 && $relativeStrength < 2.75) return "EliteIII";
        if ($relativeStrength >= 2.75 && $relativeStrength < 3.0) return "EliteII";
        return "EliteI";
    }
}