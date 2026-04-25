<?php

namespace Models;

class Profile
{
    private ?int $profileId;
    private int $userId;
    private int $bodyweight;
    
    public function __construct()
    {
        $this->profileId = null;
    }
    
    // Getters
    public function getProfileId(): ?int
    {
        return $this->profileId;
    }
    
    public function getUserId(): int
    {
        return $this->userId;
    }
    
    public function getBodyweight(): int
    {
        return $this->bodyweight;
    }
    
    // Setters
    public function setProfileId(?int $profileId): void
    {
        $this->profileId = $profileId;
    }
    
    public function setUserId(int $userId): void
    {
        $this->userId = $userId;
    }
    
    public function setBodyweight(int $bodyweight): void
    {
        // Validate range 0-300 (same as ASP.NET)
        if ($bodyweight < 0 || $bodyweight > 300) {
            throw new \InvalidArgumentException("Bodyweight must be between 0 and 300");
        }
        $this->bodyweight = $bodyweight;
    }
    
    // Convert to array
    public function toArray(): array
    {
        return [
            'profileId' => $this->profileId,
            'userId' => $this->userId,
            'bodyweight' => $this->bodyweight
        ];
    }
    
    // Create from database row
    public static function fromArray(array $data): Profile
    {
        $profile = new Profile();
        $profile->setProfileId($data['profile_id'] ?? null);
        $profile->setUserId($data['user_id'] ?? 0);
        $profile->setBodyweight($data['bodyweight'] ?? 0);
        
        return $profile;
    }
}