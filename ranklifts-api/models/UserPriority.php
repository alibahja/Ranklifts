<?php

namespace Models;

class UserPriority
{
    private int $userId;
    private string $movementType;
    private string $firstPriority;
    private string $secondPriority;
    private string $thirdPriority;
    private string $fourthPriority;
    private string $updatedAt;
    
    public function __construct()
    {
        $this->updatedAt = date('Y-m-d H:i:s');
    }
    
    // Getters
    public function getUserId(): int { return $this->userId; }
    public function getMovementType(): string { return $this->movementType; }
    public function getFirstPriority(): string { return $this->firstPriority; }
    public function getSecondPriority(): string { return $this->secondPriority; }
    public function getThirdPriority(): string { return $this->thirdPriority; }
    public function getFourthPriority(): string { return $this->fourthPriority; }
    public function getUpdatedAt(): string { return $this->updatedAt; }
    
    // Setters
    public function setUserId(int $userId): void { $this->userId = $userId; }
    public function setMovementType(string $movementType): void { $this->movementType = $movementType; }
    public function setFirstPriority(string $priority): void { $this->firstPriority = $priority; }
    public function setSecondPriority(string $priority): void { $this->secondPriority = $priority; }
    public function setThirdPriority(string $priority): void { $this->thirdPriority = $priority; }
    public function setFourthPriority(string $priority): void { $this->fourthPriority = $priority; }
    public function setUpdatedAt(string $updatedAt): void { $this->updatedAt = $updatedAt; }
    
    public function toArray(): array
    {
        return [
            'userId' => $this->userId,
            'movementType' => $this->movementType,
            'firstPriority' => $this->firstPriority,
            'secondPriority' => $this->secondPriority,
            'thirdPriority' => $this->thirdPriority,
            'fourthPriority' => $this->fourthPriority,
            'updatedAt' => $this->updatedAt
        ];
    }
    
    public static function fromArray(array $data): UserPriority
    {
        $priority = new UserPriority();
        $priority->setUserId($data['user_id'] ?? 0);
        $priority->setMovementType($data['movement_type'] ?? '');
        $priority->setFirstPriority($data['first_priority'] ?? '');
        $priority->setSecondPriority($data['second_priority'] ?? '');
        $priority->setThirdPriority($data['third_priority'] ?? '');
        $priority->setFourthPriority($data['fourth_priority'] ?? '');
        $priority->setUpdatedAt($data['updated_at'] ?? date('Y-m-d H:i:s'));
        return $priority;
    }
}