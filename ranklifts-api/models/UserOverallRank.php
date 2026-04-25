<?php

namespace Models;

class UserOverallRank
{
    private ?int $overallId;
    private int $userId;
    private float $overallScore;
    private string $rank;
    private string $calculatedAt;
    
    public function __construct()
    {
        $this->overallId = null;
        $this->calculatedAt = date('Y-m-d H:i:s');
    }
    
    // Getters
    public function getOverallId(): ?int
    {
        return $this->overallId;
    }
    
    public function getUserId(): int
    {
        return $this->userId;
    }
    
    public function getOverallScore(): float
    {
        return $this->overallScore;
    }
    
    public function getRank(): string
    {
        return $this->rank;
    }
    
    public function getCalculatedAt(): string
    {
        return $this->calculatedAt;
    }
    
    // Setters
    public function setOverallId(?int $overallId): void
    {
        $this->overallId = $overallId;
    }
    
    public function setUserId(int $userId): void
    {
        $this->userId = $userId;
    }
    
    public function setOverallScore(float $overallScore): void
    {
        $this->overallScore = $overallScore;
    }
    
    public function setRank(string $rank): void
    {
        $this->rank = $rank;
    }
    
    public function setCalculatedAt(string $calculatedAt): void
    {
        $this->calculatedAt = $calculatedAt;
    }
    
    // Convert to array
    public function toArray(): array
    {
        return [
            'overallId' => $this->overallId,
            'userId' => $this->userId,
            'overallScore' => $this->overallScore,
            'rank' => $this->rank,
            'calculatedAt' => $this->calculatedAt
        ];
    }
    
    // Create from database row
    public static function fromArray(array $data): UserOverallRank
    {
        $overallRank = new UserOverallRank();
        $overallRank->setOverallId($data['overall_id'] ?? null);
        $overallRank->setUserId($data['user_id'] ?? 0);
        $overallRank->setOverallScore((float)($data['overall_score'] ?? 0));
        $overallRank->setRank($data['rank'] ?? '');
        $overallRank->setCalculatedAt($data['calculated_at'] ?? date('Y-m-d H:i:s'));
        
        return $overallRank;
    }
}