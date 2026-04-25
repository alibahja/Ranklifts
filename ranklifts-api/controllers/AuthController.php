<?php

namespace Controllers;

use Services\AuthService;
use Middleware\AuthMiddleware;

class AuthController
{
    private AuthService $authService;
    
    public function __construct()
    {
        $this->authService = new AuthService();
    }
    
    /**
     * POST /api/auth/register
     * Request body: { "username": "string", "email": "string", "password": "string" }
     */
    public function register(): void
    {
        try {
            // Get request body
            $data = json_decode(file_get_contents('php://input'), true);
            
            // Validate required fields
            if (!isset($data['username']) || !isset($data['email']) || !isset($data['password'])) {
                throw new \Exception('Username, email, and password are required');
            }
            
            // Validate email format
            if (!filter_var($data['email'], FILTER_VALIDATE_EMAIL)) {
                throw new \Exception('Invalid email format');
            }
            
            // Validate password length
            if (strlen($data['password']) < 6) {
                throw new \Exception('Password must be at least 6 characters');
            }
            
            // Register user
            $result = $this->authService->register(
                $data['username'],
                $data['email'],
                $data['password']
            );
            
            // Return success response
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Exception $e) {
            http_response_code(400);
            echo json_encode([
                'error' => $e->getMessage(),
                'timestamp' => date('Y-m-d H:i:s')
            ]);
        }
    }
    
    /**
     * POST /api/auth/login
     * Request body: { "email": "string", "password": "string" }
     */
    public function login(): void
    {
        try {
            // Get request body
            $data = json_decode(file_get_contents('php://input'), true);
            
            // Validate required fields
            if (!isset($data['email']) || !isset($data['password'])) {
                throw new \Exception('Email and password are required');
            }
            
            // Login user
            $result = $this->authService->login($data['email'], $data['password']);
            
            // Return success response
            http_response_code(200);
            echo json_encode($result);
            
        } catch (\Services\UnauthorizedException $e) {
            http_response_code(401);
            echo json_encode([
                'error' => $e->getMessage(),
                'timestamp' => date('Y-m-d H:i:s')
            ]);
        } catch (\Exception $e) {
            http_response_code(400);
            echo json_encode([
                'error' => $e->getMessage(),
                'timestamp' => date('Y-m-d H:i:s')
            ]);
        }
    }
}