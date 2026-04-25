<?php

namespace Controllers;

use Services\UserCategoryService;
use Middleware\AuthMiddleware;

class CategoryRankController
{
    private UserCategoryService $userCategoryService;
    private AuthMiddleware $authMiddleware;
    
    public function __construct()
    {
        $this->userCategoryService = new UserCategoryService();
        $this->authMiddleware = new AuthMiddleware();
    }
    
    /**
     * POST /api/categoryrank
     * Request body: { 
     *     "movementType": "string", 
     *     "firstPriority": "string", 
     *     "secondPriority": "string", 
     *     "thirdPriority": "string", 
     *     "fourthPriority": "string" 
     * }
     */
    public function calculateCategoryRank(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            $data = json_decode(file_get_contents('php://input'), true);
            
            // Validate required fields
            if (!isset($data['movementType'])) {
                throw new \Exception('movementType is required');
            }
            if (!isset($data['firstPriority'])) {
                throw new \Exception('firstPriority is required');
            }
            if (!isset($data['secondPriority'])) {
                throw new \Exception('secondPriority is required');
            }
            if (!isset($data['thirdPriority'])) {
                throw new \Exception('thirdPriority is required');
            }
            if (!isset($data['fourthPriority'])) {
                throw new \Exception('fourthPriority is required');
            }
            
            // Validate movement type
            $movementType = strtolower($data['movementType']);
            if (!in_array($movementType, ['pull', 'push', 'legs'])) {
                throw new \Exception('movementType must be "Pull", "Push", or "Legs"');
            }
            
            $result = $this->userCategoryService->calculateUserCategoryRank(
                $userId,
                $data['movementType'],
                $data['firstPriority'],
                $data['secondPriority'],
                $data['thirdPriority'],
                $data['fourthPriority']
            );
            
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    /**
     * PUT /api/categoryrank
     * Request body: { 
     *     "movementType": "string", 
     *     "firstPriority": "string", 
     *     "secondPriority": "string", 
     *     "thirdPriority": "string", 
     *     "fourthPriority": "string" 
     * }
     */
    public function updateCategoryRank(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            $data = json_decode(file_get_contents('php://input'), true);
            
            // Validate required fields
            if (!isset($data['movementType'])) {
                throw new \Exception('movementType is required');
            }
            if (!isset($data['firstPriority'])) {
                throw new \Exception('firstPriority is required');
            }
            if (!isset($data['secondPriority'])) {
                throw new \Exception('secondPriority is required');
            }
            if (!isset($data['thirdPriority'])) {
                throw new \Exception('thirdPriority is required');
            }
            if (!isset($data['fourthPriority'])) {
                throw new \Exception('fourthPriority is required');
            }
            
            // Validate movement type
            $movementType = strtolower($data['movementType']);
            if (!in_array($movementType, ['pull', 'push', 'legs'])) {
                throw new \Exception('movementType must be "Pull", "Push", or "Legs"');
            }
            
            $result = $this->userCategoryService->updateUserCategoryRank(
                $userId,
                $data['movementType'],
                $data['firstPriority'],
                $data['secondPriority'],
                $data['thirdPriority'],
                $data['fourthPriority']
            );
            
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    /**
     * GET /api/categoryrank/{movementType}
     */
    public function getCategoryRank($movementType): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            
            // Validate movement type
            $normalizedType = strtolower($movementType);
            if (!in_array($normalizedType, ['pull', 'push', 'legs'])) {
                throw new \Exception('movementType must be "Pull", "Push", or "Legs"');
            }
            
            $result = $this->userCategoryService->getUserCategoryRank($userId, $movementType);
            
            if ($result === null) {
                http_response_code(404);
                echo json_encode([
                    'error' => 'Category rank not found for this movement type',
                    'timestamp' => date('Y-m-d H:i:s')
                ]);
                return;
            }
            
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
        } else {
            http_response_code(400);
        }
        
        echo json_encode([
            'error' => $message,
            'timestamp' => date('Y-m-d H:i:s')
        ]);
    }
}