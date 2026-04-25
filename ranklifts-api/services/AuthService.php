<?php

namespace Services;

use Config\JwtConfig;
use Repositories\UserRepo;
use Models\User;



class AuthService
{
    private UserRepo $userRepo;
    private JwtConfig $jwtConfig;
    
    public function __construct()
    {
        $this->userRepo = new UserRepo();
        $this->jwtConfig = JwtConfig::getInstance();
    }
    
    /**
     * Register a new user
     */
    public function register(string $username, string $email, string $password): array
    {
        // Check if user already exists
        if ($this->userRepo->existsByEmail($email)) {
            throw new \Exception("User with this email already exists");
        }
        
        // Create new user
        $user = new User();
        $user->setUsername($username);
        $user->setEmail($email);
        $user->setPasswordHash(password_hash($password, PASSWORD_BCRYPT));
        $user->setCreatedAt(date('Y-m-d H:i:s'));
        
        $this->userRepo->add($user);
        
        // Generate JWT token
        $token = $this->jwtConfig->generateToken(
            $user->getUserId(),
            $user->getEmail(),
            $user->getUsername()
        );
        
        return [
            'userId' => $user->getUserId(),
            'username' => $user->getUsername(),
            'email' => $user->getEmail(),
            'token' => $token,
            'hasCompletedProfile' => false
        ];
    }
    
    /**
     * Login user
     */
    public function login(string $email, string $password): array
    {
        // Get user by email
        $user = $this->userRepo->getByEmail($email);
        
        if (!$user) {
            throw new UnauthorizedException("Invalid email");
        }
        
        // Verify password
        if (!password_verify($password, $user->getPasswordHash())) {
            throw new UnauthorizedException("Invalid password");
        }
        
        // Generate JWT token
        $token = $this->jwtConfig->generateToken(
            $user->getUserId(),
            $user->getEmail(),
            $user->getUsername()
        );
        
        // Check if profile exists
        $hasCompletedProfile = $user->getProfile() !== null;
        
        return [
            'userId' => $user->getUserId(),
            'username' => $user->getUsername(),
            'email' => $user->getEmail(),
            'token' => $token,
            'hasCompletedProfile' => $hasCompletedProfile
        ];
    }
    
    /**
     * Validate token and get user
     */
    public function validateToken(string $token): ?object
    {
        return $this->jwtConfig->validateToken($token);
    }
    
    /**
     * Get user ID from token
     */
    public function getUserIdFromToken(string $token): ?int
    {
        return $this->jwtConfig->getUserIdFromToken($token);
    }
}

// Custom exception for unauthorized access
class UnauthorizedException extends \Exception {}