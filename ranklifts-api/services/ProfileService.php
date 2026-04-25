<?php

namespace Services;

use Repositories\ProfileRepo;
use Repositories\UserRepo;
use Models\Profile;

class ProfileService
{
    private ProfileRepo $profileRepo;
    private UserRepo $userRepo;
    
    public function __construct()
    {
        $this->profileRepo = new ProfileRepo();
        $this->userRepo = new UserRepo();
    }
    
    /**
     * Create profile for user
     */
    public function createProfile(int $userId, int $bodyweight): array
    {
        // Check if profile already exists
        $existingProfile = $this->profileRepo->getByUserId($userId);
        if ($existingProfile) {
            throw new \Exception("Profile already exists for this user.");
        }
        
        // Get user
        $user = $this->userRepo->getById($userId);
        if (!$user) {
            throw new \Exception("User not found.");
        }
        
        // Create profile
        $profile = new Profile();
        $profile->setUserId($userId);
        $profile->setBodyweight($bodyweight);
        
        $this->profileRepo->create($profile);
        
        // Update user with profile reference
        $user->setProfile($profile);
        $this->userRepo->update($user);
        
        return [
            'profileId' => $profile->getProfileId(),
            'userId' => $profile->getUserId(),
            'bodyweight' => $profile->getBodyweight()
        ];
    }
    
    /**
     * Get profile by user ID
     */
    public function getProfile(int $userId): array
    {
        $profile = $this->profileRepo->getByUserId($userId);
        
        if (!$profile) {
            throw new \Exception("Profile not found for this user.");
        }
        
        return [
            'profileId' => $profile->getProfileId(),
            'userId' => $profile->getUserId(),
            'bodyweight' => $profile->getBodyweight()
        ];
    }
    
    /**
     * Update profile
     */
    public function updateProfile(int $userId, int $bodyweight): array
    {
        $profile = $this->profileRepo->getByUserId($userId);
        
        if (!$profile) {
            throw new \Exception("Profile not found for this user.");
        }
        
        $profile->setBodyweight($bodyweight);
        $this->profileRepo->update($profile);
        
        return [
            'profileId' => $profile->getProfileId(),
            'userId' => $profile->getUserId(),
            'bodyweight' => $profile->getBodyweight()
        ];
    }
}