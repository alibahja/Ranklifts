<?php

namespace Config;

use Exception;
use Firebase\JWT\JWT;
use Firebase\JWT\Key;

class JwtConfig
{
    private static ?JwtConfig $instance = null;
    private string $secretKey;
    private string $issuer;
    private string $audience;
    private int $expirySeconds;
    
    private function __construct()
    {
        // JWT Configuration
        // IMPORTANT: Change this to a strong random key in production!
        $this->secretKey = 'RankLiftsSuperSecretKey2024!@#$%^&*()';
        $this->issuer = 'ranklifts-api';
        $this->audience = 'ranklifts-app';
        $this->expirySeconds = 7 * 24 * 60 * 60; // 7 days (same as ASP.NET)
    }
    
    public static function getInstance(): JwtConfig
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }
    
    /**
     * Generate JWT token for a user
     */
    public function generateToken(int $userId, string $email, string $username): string
    {
        $issuedAt = time();
        $expireAt = $issuedAt + $this->expirySeconds;
        
        $payload = [
            'iat' => $issuedAt,
            'exp' => $expireAt,
            'iss' => $this->issuer,
            'aud' => $this->audience,
            'sub' => (string)$userId,
            'jti' => bin2hex(random_bytes(16)),
            'userId' => $userId,
            'email' => $email,
            'username' => $username
        ];
        
        return JWT::encode($payload, $this->secretKey, 'HS256');
    }
    
    /**
     * Validate and decode JWT token
     */
    public function validateToken(string $token): ?object
    {
        try {
            $decoded = JWT::decode($token, new Key($this->secretKey, 'HS256'));
            
            // Verify issuer
            if ($decoded->iss !== $this->issuer) {
                return null;
            }
            
            // Verify audience
            if ($decoded->aud !== $this->audience) {
                return null;
            }
            
            // Check expiration
            if ($decoded->exp < time()) {
                return null;
            }
            
            return $decoded;
            
        } catch (Exception $e) {
            return null;
        }
    }
    
    /**
     * Get user ID from token
     */
    public function getUserIdFromToken(string $token): ?int
    {
        $decoded = $this->validateToken($token);
        return $decoded ? (int)$decoded->userId : null;
    }
    
    /**
     * Get secret key
     */
    public function getSecretKey(): string
    {
        return $this->secretKey;
    }
    
    /**
     * Get expiry in seconds
     */
    public function getExpirySeconds(): int
    {
        return $this->expirySeconds;
    }
}