<?php

namespace Controllers;

use Services\UserOverallRankService;
use Services\UserCategoryService;
use Services\ExercisePerformanceService;
use Middleware\AuthMiddleware;

class RankingsController
{
    private UserOverallRankService $overallRankService;
    private UserCategoryService $categoryService;
    private ExercisePerformanceService $exerciseService;
    private AuthMiddleware $authMiddleware;
    
    public function __construct()
    {
        $this->overallRankService = new UserOverallRankService();
        $this->categoryService = new UserCategoryService();
        $this->exerciseService = new ExercisePerformanceService();
        $this->authMiddleware = new AuthMiddleware();
    }
    
    /**
     * POST /api/rankings/recalculate-all
     * Recalculate all rankings using saved priorities
     */
    public function recalculateAll(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            
            // Get all exercise performances
            $pushExercises = $this->exerciseService->getUserPushPerformance($userId);
            $pullExercises = $this->exerciseService->getUserPullPerformance($userId);
            $legsExercises = $this->exerciseService->getUserLegsPerformance($userId);
            
            if (empty($pushExercises) && empty($pullExercises) && empty($legsExercises)) {
                throw new \Exception("No exercises found. Please add exercises first.");
            }
            
            // Recalculate each movement category (uses saved priorities automatically)
            $pushCategory = null;
            $pullCategory = null;
            $legsCategory = null;
            
            if (!empty($pushExercises)) {
                $pushCategory = $this->categoryService->updateUserCategoryRank($userId, 'push');
            }
            
            if (!empty($pullExercises)) {
                $pullCategory = $this->categoryService->updateUserCategoryRank($userId, 'pull');
            }
            
            if (!empty($legsExercises)) {
                $legsCategory = $this->categoryService->updateUserCategoryRank($userId, 'legs');
            }
            
            // Recalculate overall rank
            $overallRank = null;
            if (!empty($pushExercises) || !empty($pullExercises) || !empty($legsExercises)) {
               // RankingsController.php — inside recalculateAll(), replace the overall rank block
try {
    $existingRank = $this->overallRankService->getUserOverallRank($userId);
    if ($existingRank) {
        $overallRank = $this->overallRankService->updateUserOverallRank($userId);
    } else {
        $overallRank = $this->overallRankService->createUserOverallRank($userId);
    }
} catch (\Exception $e) {
    // If update fails, try create as fallback
    try {
        $overallRank = $this->overallRankService->createUserOverallRank($userId);
    } catch (\Exception $e2) {
        error_log("Failed to create overall rank: " . $e2->getMessage());
        $overallRank = null;
    }
}
            }
            
            $response = [
                'success' => true,
                'message' => 'All rankings recalculated successfully!',
                'timestamp' => date('Y-m-d H:i:s'),
                'pushCategory' => $pushCategory,
                'pullCategory' => $pullCategory,
                'legsCategory' => $legsCategory,
                'overallRank' => $overallRank
            ];
            
            http_response_code(200);
            echo json_encode($response);
            
        } catch (\Exception $e) {
            $this->sendRecalculationErrorResponse($e);
        }
    }
    
    /**
     * POST /api/rankings/recalculate-all-profile
     * Recalculate all rankings after updating each exercise performance
     */
    public function recalculateAllProfile(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            
            // Get all exercise performances
            $pushExercises = $this->exerciseService->getUserPushPerformance($userId);
            $pullExercises = $this->exerciseService->getUserPullPerformance($userId);
            $legsExercises = $this->exerciseService->getUserLegsPerformance($userId);
            
            // Update each exercise performance (recalculates ranks)
            foreach ($pushExercises as $exercise) {
                $this->exerciseService->updateExerciseRank(
                    $userId,
                    $exercise['performanceId'],
                    $exercise['exerciseName'],
                    $exercise['addedWeight'],
                    $exercise['reps']
                );
            }
            
            foreach ($pullExercises as $exercise) {
                $this->exerciseService->updateExerciseRank(
                    $userId,
                    $exercise['performanceId'],
                    $exercise['exerciseName'],
                    $exercise['addedWeight'],
                    $exercise['reps']
                );
            }
            
            foreach ($legsExercises as $exercise) {
                $this->exerciseService->updateExerciseRank(
                    $userId,
                    $exercise['performanceId'],
                    $exercise['exerciseName'],
                    $exercise['addedWeight'],
                    $exercise['reps']
                );
            }
            
            // Refresh exercise lists after updates
            $pushExercises = $this->exerciseService->getUserPushPerformance($userId);
            $pullExercises = $this->exerciseService->getUserPullPerformance($userId);
            $legsExercises = $this->exerciseService->getUserLegsPerformance($userId);
            
            if (empty($pushExercises) && empty($pullExercises) && empty($legsExercises)) {
                throw new \Exception("No exercises found. Please add exercises first.");
            }
            
            // Recalculate category ranks (uses saved priorities)
            $pushCategory = null;
            $pullCategory = null;
            $legsCategory = null;
            
            if (!empty($pushExercises)) {
                $pushCategory = $this->categoryService->updateUserCategoryRank($userId, 'push');
            }
            
            if (!empty($pullExercises)) {
                $pullCategory = $this->categoryService->updateUserCategoryRank($userId, 'pull');
            }
            
            if (!empty($legsExercises)) {
                $legsCategory = $this->categoryService->updateUserCategoryRank($userId, 'legs');
            }
            
            // Recalculate overall rank
            $overallRank = null;
            if (!empty($pushExercises) || !empty($pullExercises) || !empty($legsExercises)) {
                try {
                    $overallRank = $this->overallRankService->getUserOverallRank($userId);
                    if ($overallRank) {
                        $overallRank = $this->overallRankService->updateUserOverallRank($userId);
                    } else {
                        $overallRank = $this->overallRankService->createUserOverallRank($userId);
                    }
                } catch (\Exception $e) {
                    $overallRank = $this->overallRankService->createUserOverallRank($userId);
                }
            }
            
            $response = [
                'success' => true,
                'message' => 'All rankings recalculated successfully!',
                'timestamp' => date('Y-m-d H:i:s'),
                'pushCategory' => $pushCategory,
                'pullCategory' => $pullCategory,
                'legsCategory' => $legsCategory,
                'overallRank' => $overallRank
            ];
            
            http_response_code(200);
            echo json_encode($response);
            
        } catch (\Exception $e) {
            $this->sendRecalculationErrorResponse($e);
        }
    }
    
    /**
     * GET /api/rankings/status
     */
    public function getRankingsStatus(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            
            $pushExercises = $this->exerciseService->getUserPushPerformance($userId);
            $pullExercises = $this->exerciseService->getUserPullPerformance($userId);
            $legsExercises = $this->exerciseService->getUserLegsPerformance($userId);
            
            $pushCategory = null;
            $pullCategory = null;
            $legsCategory = null;
            $overallRank = null;
            
            try {
                $pushCategory = $this->categoryService->getUserCategoryRank($userId, 'push');
            } catch (\Exception $e) {}
            
            try {
                $pullCategory = $this->categoryService->getUserCategoryRank($userId, 'pull');
            } catch (\Exception $e) {}
            
            try {
                $legsCategory = $this->categoryService->getUserCategoryRank($userId, 'legs');
            } catch (\Exception $e) {}
            
            try {
                $overallRank = $this->overallRankService->getUserOverallRank($userId);
            } catch (\Exception $e) {}
            
            $response = [
                'hasPushExercises' => !empty($pushExercises),
                'hasPullExercises' => !empty($pullExercises),
                'hasLegsExercises' => !empty($legsExercises),
                'pushExercisesCount' => count($pushExercises),
                'pullExercisesCount' => count($pullExercises),
                'legsExercisesCount' => count($legsExercises),
                'hasPushCategory' => $pushCategory !== null,
                'hasPullCategory' => $pullCategory !== null,
                'hasLegsCategory' => $legsCategory !== null,
                'hasOverallRank' => $overallRank !== null,
                'lastUpdated' => date('Y-m-d H:i:s')
            ];
            
            http_response_code(200);
            echo json_encode($response);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    private function sendRecalculationErrorResponse(\Exception $e): void
    {
        $response = [
            'success' => false,
            'message' => 'Recalculation failed: ' . $e->getMessage(),
            'timestamp' => date('Y-m-d H:i:s')
        ];
        
        http_response_code(400);
        echo json_encode($response);
    }
    
    private function sendErrorResponse(\Exception $e): void
    {
        $message = $e->getMessage();
        
        if ($message === 'Authorization header not found' || $message === 'Invalid or expired token') {
            http_response_code(401);
        } else {
            http_response_code(400);
        }
        
        echo json_encode([
            'error' => $message,
            'timestamp' => date('Y-m-d H:i:s')
        ]);
    }
}