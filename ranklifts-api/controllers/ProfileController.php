<?php

namespace Controllers;

use Services\ProfileService;
use Middleware\AuthMiddleware;

class ProfileController
{
    private ProfileService $profileService;
    private AuthMiddleware $authMiddleware;
    
    public function __construct()
    {
        $this->profileService = new ProfileService();
        $this->authMiddleware = new AuthMiddleware();
    }
    
    /**
     * GET /api/profile
     * Get profile for authenticated user
     */
    public function getProfile(): void
    {
        try {
            // Authenticate user
            $userId = $this->authMiddleware->authenticate();
            
            // Get profile
            $result = $this->profileService->getProfile($userId);
            
            // Return success response
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            if ($e->getMessage() === 'Profile not found for this user.') {
                http_response_code(404);
            } else if ($e->getMessage() === 'Authorization header not found' || 
                       $e->getMessage() === 'Invalid or expired token') {
                http_response_code(401);
            } else {
                http_response_code(400);
            }
            echo json_encode([
                'error' => $e->getMessage(),
                'timestamp' => date('Y-m-d H:i:s')
            ]);
        }
    }
    
    /**
     * POST /api/profile
     * Request body: { "bodyweight": int }
     * Create profile for authenticated user
     */
    public function createProfile(): void
    {
        try {
            // Authenticate user
            $userId = $this->authMiddleware->authenticate();
            
            // Get request body
            $data = json_decode(file_get_contents('php://input'), true);
            
            // Validate required fields
            if (!isset($data['bodyweight'])) {
                throw new \Exception('Bodyweight is required');
            }
            
            // Validate bodyweight range (0-300)
            if ($data['bodyweight'] < 0 || $data['bodyweight'] > 300) {
                throw new \Exception('Bodyweight must be between 0 and 300');
            }
            
            // Create profile
            $result = $this->profileService->createProfile($userId, $data['bodyweight']);
            
            // Return success response
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            if ($e->getMessage() === 'Authorization header not found' || 
                $e->getMessage() === 'Invalid or expired token') {
                http_response_code(401);
            } else if ($e->getMessage() === 'Profile already exists for this user.') {
                http_response_code(400);
            } else {
                http_response_code(400);
            }
            echo json_encode([
                'error' => $e->getMessage(),
                'timestamp' => date('Y-m-d H:i:s')
            ]);
        }
    }
    
    /**
     * PUT /api/profile
     * Request body: { "bodyweight": int }
     * Update profile for authenticated user
     */
    public function updateProfile(): void
    {
        try {
            // Authenticate user
            $userId = $this->authMiddleware->authenticate();
            
            // Get request body
            $data = json_decode(file_get_contents('php://input'), true);
            
            // Validate required fields
            if (!isset($data['bodyweight'])) {
                throw new \Exception('Bodyweight is required');
            }
            
            // Validate bodyweight range (0-300)
            if ($data['bodyweight'] < 0 || $data['bodyweight'] > 300) {
                throw new \Exception('Bodyweight must be between 0 and 300');
            }
            
            // Update profile
            $result = $this->profileService->updateProfile($userId, $data['bodyweight']);
            
            // Return success response
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            if ($e->getMessage() === 'Authorization header not found' || 
                $e->getMessage() === 'Invalid or expired token') {
                http_response_code(401);
            } else if ($e->getMessage() === 'Profile not found for this user.') {
                http_response_code(404);
            } else {
                http_response_code(400);
            }
            echo json_encode([
                'error' => $e->getMessage(),
                'timestamp' => date('Y-m-d H:i:s')
            ]);
        }
    }
}