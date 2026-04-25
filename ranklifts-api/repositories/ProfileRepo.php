<?php

namespace Repositories;

use Config\Database;
use Models\Profile;
use PDO;

class ProfileRepo
{
    private PDO $db;
    
    public function __construct()
    {
        $this->db = Database::getInstance()->getConnection();
    }
    
    /**
     * Create a new profile
     */
    public function create(Profile $profile): void
    {
        $sql = "INSERT INTO profiles (user_id, bodyweight) VALUES (:user_id, :bodyweight)";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':user_id' => $profile->getUserId(),
            ':bodyweight' => $profile->getBodyweight()
        ]);
        
        // Set the auto-generated ID
        $profile->setProfileId((int)$this->db->lastInsertId());
    }
    
    /**
     * Get profile by profile ID
     */
    public function getByProfileId(int $id): ?Profile
    {
        $sql = "SELECT * FROM profiles WHERE profile_id = :id";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':id' => $id]);
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$data) {
            return null;
        }
        
        return Profile::fromArray($data);
    }
    
    /**
     * Get profile by user ID
     */
    public function getByUserId(int $userId): ?Profile
    {
        $sql = "SELECT * FROM profiles WHERE user_id = :user_id";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':user_id' => $userId]);
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$data) {
            return null;
        }
        
        return Profile::fromArray($data);
    }
    
    /**
     * Update profile
     */
    public function update(Profile $profile): void
    {
        $sql = "UPDATE profiles SET bodyweight = :bodyweight WHERE profile_id = :profile_id";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':bodyweight' => $profile->getBodyweight(),
            ':profile_id' => $profile->getProfileId()
        ]);
    }
    
    /**
     * Check if profile exists for user
     */
    public function existsByUserId(int $userId): bool
    {
        $sql = "SELECT COUNT(*) FROM profiles WHERE user_id = :user_id";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':user_id' => $userId]);
        return (int)$stmt->fetchColumn() > 0;
    }
}