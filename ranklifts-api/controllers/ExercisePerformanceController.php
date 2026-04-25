<?php

namespace Controllers;

use Services\ExercisePerformanceService;
use Middleware\AuthMiddleware;

class ExercisePerformanceController
{
    private ExercisePerformanceService $service;
    private AuthMiddleware $authMiddleware;
    
    public function __construct()
    {
        $this->service = new ExercisePerformanceService();
        $this->authMiddleware = new AuthMiddleware();
    }
    
    /**
     * POST /api/exercise-performance
     * Request body: { "exerciseName": "string", "addedWeight": float, "reps": int }
     */
    public function calculateExerciseRank(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            $data = json_decode(file_get_contents('php://input'), true);
            
            if (!isset($data['exerciseName']) || !isset($data['addedWeight']) || !isset($data['reps'])) {
                throw new \Exception('exerciseName, addedWeight, and reps are required');
            }
            
            $result = $this->service->calculateExerciseRank(
                $userId,
                $data['exerciseName'],
                (float)$data['addedWeight'],
                (int)$data['reps']
            );
            
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    /**
     * PUT /api/exercise-performance/{performanceId}
     * Request body: { "exerciseName": "string", "addedWeight": float, "reps": int }
     */
    public function updateExerciseRank($performanceId): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            $data = json_decode(file_get_contents('php://input'), true);
            
            if (!isset($data['exerciseName']) || !isset($data['addedWeight']) || !isset($data['reps'])) {
                throw new \Exception('exerciseName, addedWeight, and reps are required');
            }
            
            $result = $this->service->updateExerciseRank(
                $userId,
                (int)$performanceId,
                $data['exerciseName'],
                (float)$data['addedWeight'],
                (int)$data['reps']
            );
            
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    /**
     * GET /api/exercise-performance/{id}
     */
    public function getExercisePerformance($id): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            
            $result = $this->service->getExercisePerformanceById($userId, (int)$id);
            
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    /**
     * DELETE /api/exercise-performance/{id}
     */
    public function deleteExercisePerformance($id): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            
            $result = $this->service->deleteExercisePerformance($userId, (int)$id);
            
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    /**
     * POST /api/exercise-performance/batch-update
     * Request body: [ { "performanceId": int|null, "exerciseName": "string", "addedWeight": float, "reps": int } ]
     */
    public function updateExerciseBatch(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            $data = json_decode(file_get_contents('php://input'), true);
            
            if (!is_array($data)) {
                throw new \Exception('Request body must be an array of exercises');
            }
            
            $results = $this->service->updateExerciseBatch($userId, $data);
            
            http_response_code(200);
            echo json_encode($results);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    /**
     * GET /api/exercise-performance/pull
     */
    public function getPullPerformance(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            
            $result = $this->service->getUserPullPerformance($userId);
            
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    /**
     * GET /api/exercise-performance/push
     */
    public function getPushPerformance(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            
            $result = $this->service->getUserPushPerformance($userId);
            
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    /**
     * GET /api/exercise-performance/legs
     */
    public function getLegsPerformance(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            
            $result = $this->service->getUserLegsPerformance($userId);
            
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    private function sendErrorResponse(\Exception $e): void
    {
        $message = $e->getMessage();
        
        if ($message === 'Authorization header not found' || $message === 'Invalid or expired token') {
            http_response_code(401);
        } else if ($message === 'Exercise performance not found or access denied.' || 
                   $message === 'Performance not found.') {
            http_response_code(404);
        } else {
            http_response_code(400);
        }
        
        echo json_encode([
            'error' => $message,
            'timestamp' => date('Y-m-d H:i:s')
        ]);
    }
    public function batchUpdate(): void
{
    try {
        $userId = $this->authMiddleware->authenticate();
        $data = json_decode(file_get_contents('php://input'), true);
        
        if (!is_array($data)) {
            throw new \Exception('Invalid data format. Expected an array of exercises.');
        }

        $results = [];
        foreach ($data as $item) {
            // Re-using your existing service logic for each item in the list
            $results[] = $this->service->calculateExerciseRank(
                $userId,
                $item['exerciseName'],
                (float)$item['addedWeight'],
                (int)$item['reps']
            );
        }
        
        http_response_code(200);
        echo json_encode($results);
    } catch (\Exception $e) {
        $this->sendErrorResponse($e);
    }
}
}