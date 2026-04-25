<?php

namespace Controllers;

use Services\UserOverallRankService;
use Middleware\AuthMiddleware;

class OverallRankController
{
    private UserOverallRankService $userOverallRankService;
    private AuthMiddleware $authMiddleware;
    
    public function __construct()
    {
        $this->userOverallRankService = new UserOverallRankService();
        $this->authMiddleware = new AuthMiddleware();
    }
    
    /**
     * GET /api/overallrank
     * Get overall rank for authenticated user
     */
    public function getOverallRank(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            
            $result = $this->userOverallRankService->getUserOverallRank($userId);
            
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    /**
     * POST /api/overallrank
     * Create overall rank for authenticated user
     */
    public function createOverallRank(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            
            $result = $this->userOverallRankService->createUserOverallRank($userId);
            
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            $this->sendErrorResponse($e);
        }
    }
    
    /**
     * PUT /api/overallrank
     * Update overall rank for authenticated user
     */
    public function updateOverallRank(): void
    {
        try {
            $userId = $this->authMiddleware->authenticate();
            
            $result = $this->userOverallRankService->updateUserOverallRank($userId);
            
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
        } else if ($message === 'Overall rank not found for the user.' || 
                   $message === 'All category ranks (Pull, Push, Legs) must exist for the user.') {
            http_response_code(404);
        } else {
            http_response_code(400);
        }
        
        echo json_encode([
            'error' => $message,
            'timestamp' => date('Y-m-d H:i:s')
        ]);
    }
}