<?php

namespace Models;

class User
{
    private ?int $userId;
    private string $username;
    private string $email;
    private string $passwordHash;
    private string $createdAt;
    private ?Profile $profile;
    
    public function __construct()
    {
        $this->userId = null;
        $this->profile = null;
        $this->createdAt = date('Y-m-d H:i:s');
    }
    
    // Getters
    public function getUserId(): ?int
    {
        return $this->userId;
    }
    
    public function getUsername(): string
    {
        return $this->username;
    }
    
    public function getEmail(): string
    {
        return $this->email;
    }
    
    public function getPasswordHash(): string
    {
        return $this->passwordHash;
    }
    
    public function getCreatedAt(): string
    {
        return $this->createdAt;
    }
    
    public function getProfile(): ?Profile
    {
        return $this->profile;
    }
    
    // Setters
    public function setUserId(?int $userId): void
    {
        $this->userId = $userId;
    }
    
    public function setUsername(string $username): void
    {
        $this->username = $username;
    }
    
    public function setEmail(string $email): void
    {
        $this->email = $email;
    }
    
    public function setPasswordHash(string $passwordHash): void
    {
        $this->passwordHash = $passwordHash;
    }
    
    public function setCreatedAt(string $createdAt): void
    {
        $this->createdAt = $createdAt;
    }
    
    public function setProfile(?Profile $profile): void
    {
        $this->profile = $profile;
    }
    
    // Convert to array
    public function toArray(): array
    {
        return [
            'userId' => $this->userId,
            'username' => $this->username,
            'email' => $this->email,
            'passwordHash' => $this->passwordHash,
            'createdAt' => $this->createdAt,
            'profile' => $this->profile ? $this->profile->toArray() : null
        ];
    }
    
    // Create from database row
    public static function fromArray(array $data): User
    {
        $user = new User();
        $user->setUserId($data['user_id'] ?? null);
        $user->setUsername($data['username'] ?? '');
        $user->setEmail($data['email'] ?? '');
        $user->setPasswordHash($data['password_hash'] ?? '');
        $user->setCreatedAt($data['created_at'] ?? date('Y-m-d H:i:s'));
        
        if (isset($data['profile'])) {
            $user->setProfile(Profile::fromArray($data['profile']));
        }
        
        return $user;
    }
}