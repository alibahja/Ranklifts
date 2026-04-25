<?php

namespace Repositories;

use Config\Database;
use Models\UserOverallRank;
use PDO;

class UserOverallRankRepo
{
    private PDO $db;
    
    public function __construct()
    {
        $this->db = Database::getInstance()->getConnection();
    }
    
    /**
     * Create user overall rank
     */
    public function create(UserOverallRank $overallRank): void
    {
        $sql = "INSERT INTO user_overall_rank 
                (user_id, overall_score, rank, calculated_at) 
                VALUES 
                (:user_id, :overall_score, :rank, :calculated_at)";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':user_id' => $overallRank->getUserId(),
            ':overall_score' => $overallRank->getOverallScore(),
            ':rank' => $overallRank->getRank(),
            ':calculated_at' => $overallRank->getCalculatedAt()
        ]);
        
        $overallRank->setOverallId((int)$this->db->lastInsertId());
    }
    
    /**
     * Get by user ID
     */
    public function getByUserId(int $userId): ?UserOverallRank
    {
        $sql = "SELECT * FROM user_overall_rank WHERE user_id = :user_id";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':user_id' => $userId]);
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$data) {
            return null;
        }
        
        return UserOverallRank::fromArray($data);
    }
    
    /**
     * Update user overall rank
     */
    public function update(UserOverallRank $overallRank): void
    {
        $sql = "UPDATE user_overall_rank 
                SET overall_score = :overall_score, 
                    rank = :rank, 
                    calculated_at = :calculated_at
                WHERE user_id = :user_id";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':overall_score' => $overallRank->getOverallScore(),
            ':rank' => $overallRank->getRank(),
            ':calculated_at' => $overallRank->getCalculatedAt(),
            ':user_id' => $overallRank->getUserId()
        ]);
    }
    
    /**
     * Delete by user ID
     */
    public function deleteByUserId(int $userId): void
    {
        $sql = "DELETE FROM user_overall_rank WHERE user_id = :user_id";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':user_id' => $userId]);
    }
}