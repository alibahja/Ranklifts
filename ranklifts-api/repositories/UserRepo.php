<?php

namespace Repositories;

use Config\Database;
use Models\User;
use Models\Profile;
use PDO;

class UserRepo
{
    private PDO $db;
    
    public function __construct()
    {
        $this->db = Database::getInstance()->getConnection();
    }
    
    /**
     * Add a new user
     */
    public function add(User $user): void
    {
        $sql = "INSERT INTO users (username, email, password_hash, created_at) 
                VALUES (:username, :email, :password_hash, :created_at)";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':username' => $user->getUsername(),
            ':email' => $user->getEmail(),
            ':password_hash' => $user->getPasswordHash(),
            ':created_at' => $user->getCreatedAt()
        ]);
        
        // Set the auto-generated ID
        $user->setUserId((int)$this->db->lastInsertId());
    }
    
    /**
     * Get user by email with profile included
     */
    public function getByEmail(string $email): ?User
    {
        $sql = "SELECT * FROM users WHERE email = :email";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':email' => $email]);
        $userData = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$userData) {
            return null;
        }
        
        $user = User::fromArray($userData);
        
        // Load profile
        $profile = $this->getProfileByUserId($user->getUserId());
        if ($profile) {
            $user->setProfile($profile);
        }
        
        return $user;
    }
    
    /**
     * Get user by ID with profile included
     */
    public function getById(int $id): ?User
    {
        $sql = "SELECT * FROM users WHERE user_id = :id";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':id' => $id]);
        $userData = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$userData) {
            return null;
        }
        
        $user = User::fromArray($userData);
        
        // Load profile
        $profile = $this->getProfileByUserId($user->getUserId());
        if ($profile) {
            $user->setProfile($profile);
        }
        
        return $user;
    }
    
    /**
     * Update user
     */
    public function update(User $user): void
    {
        $sql = "UPDATE users SET username = :username, email = :email, password_hash = :password_hash 
                WHERE user_id = :user_id";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':username' => $user->getUsername(),
            ':email' => $user->getEmail(),
            ':password_hash' => $user->getPasswordHash(),
            ':user_id' => $user->getUserId()
        ]);
    }
    
    /**
     * Check if user exists by email
     */
    public function existsByEmail(string $email): bool
    {
        $sql = "SELECT COUNT(*) FROM users WHERE email = :email";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':email' => $email]);
        return (int)$stmt->fetchColumn() > 0;
    }
    
    /**
     * Get profile by user ID (helper method)
     */
    private function getProfileByUserId(int $userId): ?Profile
    {
        $sql = "SELECT * FROM profiles WHERE user_id = :user_id";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':user_id' => $userId]);
        $profileData = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$profileData) {
            return null;
        }
        
        return Profile::fromArray($profileData);
    }
}