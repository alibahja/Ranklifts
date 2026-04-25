<?php

namespace Middleware;

use Config\JwtConfig;

class AuthMiddleware
{
    private JwtConfig $jwtConfig;
    
    public function __construct()
    {
        $this->jwtConfig = JwtConfig::getInstance();
    }
    
    /**
     * Authenticate request and get user ID
     * Returns user ID if authenticated, throws exception if not
     */
    public function authenticate(): int
    {
        // Get Authorization header
        $headers = $this->getAuthorizationHeader();
        
        if (!$headers) {
            throw new \Exception('Authorization header not found', 401);
        }
        
        // Extract token (Bearer token)
        $token = $this->extractToken($headers);
        
        if (!$token) {
            throw new \Exception('Invalid authorization header format. Use Bearer token', 401);
        }
        
        // Validate token
        $decoded = $this->jwtConfig->validateToken($token);
        
        if (!$decoded) {
            throw new \Exception('Invalid or expired token', 401);
        }
        
        // Return user ID from token
        return (int)$decoded->userId;
    }
    
    /**
     * Get user ID from request (returns null if not authenticated)
     */
    public function getUserIdIfAuthenticated(): ?int
    {
        try {
            return $this->authenticate();
        } catch (\Exception $e) {
            return null;
        }
    }
    
    /**
     * Get authorization header
     */
    private function getAuthorizationHeader(): ?string
    {
        // Check for Authorization header in $_SERVER
        if (isset($_SERVER['HTTP_AUTHORIZATION'])) {
            return $_SERVER['HTTP_AUTHORIZATION'];
        }
        
        // Check for HTTP_AUTHORIZATION (some servers)
        if (isset($_SERVER['REDIRECT_HTTP_AUTHORIZATION'])) {
            return $_SERVER['REDIRECT_HTTP_AUTHORIZATION'];
        }
        
        // Check for Authorization in apache_request_headers()
        if (function_exists('apache_request_headers')) {
            $headers = apache_request_headers();
            if (isset($headers['Authorization'])) {
                return $headers['Authorization'];
            }
            if (isset($headers['authorization'])) {
                return $headers['authorization'];
            }
        }
        
        return null;
    }
    
    /**
     * Extract token from Authorization header
     */
    private function extractToken(string $header): ?string
    {
        // Check for Bearer token
        if (preg_match('/Bearer\s(\S+)/', $header, $matches)) {
            return $matches[1];
        }
        
        return null;
    }
    
    /**
     * Generate JSON error response for failed auth
     */
    public static function sendUnauthorizedResponse(string $message = 'Unauthorized'): void
    {
        header('Content-Type: application/json');
        http_response_code(401);
        echo json_encode([
            'error' => $message,
            'timestamp' => date('Y-m-d H:i:s')
        ]);
        exit();
    }
    
    /**
     * Generate JSON error response for forbidden
     */
    public static function sendForbiddenResponse(string $message = 'Forbidden'): void
    {
        header('Content-Type: application/json');
        http_response_code(403);
        echo json_encode([
            'error' => $message,
            'timestamp' => date('Y-m-d H:i:s')
        ]);
        exit();
    }
}