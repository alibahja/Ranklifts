<?php

namespace Models;

class UserCategoryRank
{
    private ?int $userCategoryRankId;
    private int $userId;
    private string $movementType; // Push, Pull, Legs
    private float $categoryScore;
    private string $categoryRank;
    private string $calculatedAt;
    
    public function __construct()
    {
        $this->userCategoryRankId = null;
        $this->calculatedAt = date('Y-m-d H:i:s');
    }
    
    // Getters
    public function getUserCategoryRankId(): ?int
    {
        return $this->userCategoryRankId;
    }
    
    public function getUserId(): int
    {
        return $this->userId;
    }
    
    public function getMovementType(): string
    {
        return $this->movementType;
    }
    
    public function getCategoryScore(): float
    {
        return $this->categoryScore;
    }
    
    public function getCategoryRank(): string
    {
        return $this->categoryRank;
    }
    
    public function getCalculatedAt(): string
    {
        return $this->calculatedAt;
    }
    
    // Setters
    public function setUserCategoryRankId(?int $userCategoryRankId): void
    {
        $this->userCategoryRankId = $userCategoryRankId;
    }
    
    public function setUserId(int $userId): void
    {
        $this->userId = $userId;
    }
    
    public function setMovementType(string $movementType): void
    {
        $this->movementType = $movementType;
    }
    
    public function setCategoryScore(float $categoryScore): void
    {
        $this->categoryScore = $categoryScore;
    }
    
    public function setCategoryRank(string $categoryRank): void
    {
        $this->categoryRank = $categoryRank;
    }
    
    public function setCalculatedAt(string $calculatedAt): void
    {
        $this->calculatedAt = $calculatedAt;
    }
    
    // Convert to array
    public function toArray(): array
    {
        return [
            'userCategoryRankId' => $this->userCategoryRankId,
            'userId' => $this->userId,
            'movementType' => $this->movementType,
            'categoryScore' => $this->categoryScore,
            'categoryRank' => $this->categoryRank,
            'calculatedAt' => $this->calculatedAt
        ];
    }
    
    // Create from database row
    public static function fromArray(array $data): UserCategoryRank
    {
        $categoryRank = new UserCategoryRank();
        $categoryRank->setUserCategoryRankId($data['user_category_rank_id'] ?? null);
        $categoryRank->setUserId($data['user_id'] ?? 0);
        $categoryRank->setMovementType($data['movement_type'] ?? '');
        $categoryRank->setCategoryScore((float)($data['category_score'] ?? 0));
        $categoryRank->setCategoryRank($data['category_rank'] ?? '');
        $categoryRank->setCalculatedAt($data['calculated_at'] ?? date('Y-m-d H:i:s'));
        
        return $categoryRank;
    }
}