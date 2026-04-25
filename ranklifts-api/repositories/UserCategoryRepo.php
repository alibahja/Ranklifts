<?php

namespace Repositories;

use Config\Database;
use Models\UserCategoryRank;
use PDO;

class UserCategoryRepo
{
    private PDO $db;
    
    public function __construct()
    {
        $this->db = Database::getInstance()->getConnection();
    }
    
    /**
     * Create user category rank
     */
    public function create(UserCategoryRank $categoryRank): void
    {
        $sql = "INSERT INTO user_category_rank 
                (user_id, movement_type, category_score, category_rank, calculated_at) 
                VALUES 
                (:user_id, :movement_type, :category_score, :category_rank, :calculated_at)";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':user_id' => $categoryRank->getUserId(),
            ':movement_type' => $categoryRank->getMovementType(),
            ':category_score' => $categoryRank->getCategoryScore(),
            ':category_rank' => $categoryRank->getCategoryRank(),
            ':calculated_at' => $categoryRank->getCalculatedAt()
        ]);
        
        $categoryRank->setUserCategoryRankId((int)$this->db->lastInsertId());
    }
    
    /**
     * Get by user ID and movement type
     */
    public function getByUserId(int $userId, string $movementType): ?UserCategoryRank
    {
        $sql = "SELECT * FROM user_category_rank 
                WHERE user_id = :user_id AND movement_type = :movement_type";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':user_id' => $userId,
            ':movement_type' => $movementType
        ]);
        
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$data) {
            return null;
        }
        
        return UserCategoryRank::fromArray($data);
    }
    
    /**
     * Update user category rank
     */
    public function update(UserCategoryRank $categoryRank): void
    {
        $sql = "UPDATE user_category_rank 
                SET category_score = :category_score, 
                    category_rank = :category_rank, 
                    calculated_at = :calculated_at
                WHERE user_id = :user_id AND movement_type = :movement_type";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':category_score' => $categoryRank->getCategoryScore(),
            ':category_rank' => $categoryRank->getCategoryRank(),
            ':calculated_at' => $categoryRank->getCalculatedAt(),
            ':user_id' => $categoryRank->getUserId(),
            ':movement_type' => $categoryRank->getMovementType()
        ]);
    }
    
    /**
     * Get all category ranks for a user
     */
    public function getAllByUserId(int $userId): array
    {
        $sql = "SELECT * FROM user_category_rank WHERE user_id = :user_id";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':user_id' => $userId]);
        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        $ranks = [];
        foreach ($data as $row) {
            $ranks[] = UserCategoryRank::fromArray($row);
        }
        
        return $ranks;
    }
}