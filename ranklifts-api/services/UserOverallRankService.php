<?php

namespace Services;

use Repositories\UserOverallRankRepo;
use Models\UserOverallRank;

class UserOverallRankService
{
    private UserOverallRankRepo $overallRankRepo;
    private UserCategoryService $categoryService;
    
    public function __construct()
    {
        $this->overallRankRepo = new UserOverallRankRepo();
        $this->categoryService = new UserCategoryService();
    }
    
    /**
     * Create user overall rank
     */
    public function createUserOverallRank(int $userId): array
    {
        // Get category ranks
        $pullCategory = $this->categoryService->getUserCategoryRank($userId, 'Pull');
        $pushCategory = $this->categoryService->getUserCategoryRank($userId, 'Push');
        $legsCategory = $this->categoryService->getUserCategoryRank($userId, 'Legs');
        
        if (!$pullCategory || !$pushCategory || !$legsCategory) {
            throw new \Exception("All category ranks (Pull, Push, Legs) must exist for the user.");
        }
        
        // Calculate overall score (average of 3 categories with 0.33 weight each)
        $overallScore = ($pullCategory['categoryScore'] * 0.33) + 
                        ($pushCategory['categoryScore'] * 0.33) + 
                        ($legsCategory['categoryScore'] * 0.33);
        
        $overallRank = $this->generateRank($overallScore);
        
        // Create overall rank
        $userOverallRank = new UserOverallRank();
        $userOverallRank->setUserId($userId);
        $userOverallRank->setOverallScore($overallScore);
        $userOverallRank->setRank($overallRank);
        $userOverallRank->setCalculatedAt(date('Y-m-d H:i:s'));
        
        $this->overallRankRepo->create($userOverallRank);
        
        return [
            'overallScore' => $userOverallRank->getOverallScore(),
            'rank' => $userOverallRank->getRank(),
            'calculatedAt' => $userOverallRank->getCalculatedAt()
        ];
    }
    
    /**
     * Get user overall rank
     */
    public function getUserOverallRank(int $userId): array
    {
        $overallRank = $this->overallRankRepo->getByUserId($userId);
        
        if (!$overallRank) {
            throw new \Exception("Overall rank not found for the user.");
        }
        
        return [
            'overallScore' => $overallRank->getOverallScore(),
            'rank' => $overallRank->getRank(),
            'calculatedAt' => $overallRank->getCalculatedAt()
        ];
    }
    
    /**
     * Update user overall rank
     */
    public function updateUserOverallRank(int $userId): array
    {
        // Get existing overall rank
        $existingOverallRank = $this->overallRankRepo->getByUserId($userId);
        
        if (!$existingOverallRank) {
            throw new \Exception("Overall rank not found for the user.");
        }
        
        // Get updated category ranks
        $pullCategory = $this->categoryService->getUserCategoryRank($userId, 'Pull');
        $pushCategory = $this->categoryService->getUserCategoryRank($userId, 'Push');
        $legsCategory = $this->categoryService->getUserCategoryRank($userId, 'Legs');
        
        if (!$pullCategory || !$pushCategory || !$legsCategory) {
            throw new \Exception("All category ranks (Pull, Push, Legs) must exist for the user.");
        }
        
        // Calculate new overall score
        $overallScore = ($pullCategory['categoryScore'] * 0.33) + 
                        ($pushCategory['categoryScore'] * 0.33) + 
                        ($legsCategory['categoryScore'] * 0.33);
        
        $overallRank = $this->generateRank($overallScore);
        
        // Update existing rank
        $existingOverallRank->setOverallScore($overallScore);
        $existingOverallRank->setRank($overallRank);
        $existingOverallRank->setCalculatedAt(date('Y-m-d H:i:s'));
        
        $this->overallRankRepo->update($existingOverallRank);
        
        return [
            'overallScore' => $existingOverallRank->getOverallScore(),
            'rank' => $existingOverallRank->getRank(),
            'calculatedAt' => $existingOverallRank->getCalculatedAt()
        ];
    }
    
    /**
     * Generate rank based on overall score (different thresholds from category rank)
     */
    private function generateRank(float $overallScore): string
    {
        if ($overallScore < 0.7) return "BronzeI";
        if ($overallScore >= 0.70 && $overallScore < 0.85) return "SilverIII";
        if ($overallScore >= 0.85 && $overallScore < 1.0) return "SilverII";
        if ($overallScore >= 1.0 && $overallScore < 1.15) return "SilverI";
        if ($overallScore >= 1.15 && $overallScore < 1.30) return "GoldIII";
        if ($overallScore >= 1.30 && $overallScore < 1.45) return "GoldII";
        if ($overallScore >= 1.45 && $overallScore < 1.60) return "GoldI";
        if ($overallScore >= 1.60 && $overallScore < 1.75) return "PlatinumIII";
        if ($overallScore >= 1.75 && $overallScore < 1.9) return "PlatinumII";
        if ($overallScore >= 1.9 && $overallScore < 2.05) return "PlatinumI";
        if ($overallScore >= 2.05 && $overallScore < 2.25) return "DiamondIII";
        if ($overallScore >= 2.25 && $overallScore < 2.45) return "DiamondII";
        if ($overallScore >= 2.45 && $overallScore < 2.70) return "DiamondI";
        if ($overallScore >= 2.70 && $overallScore < 3.0) return "EliteIII";
        if ($overallScore >= 3.0 && $overallScore < 3.4) return "EliteII";
        return "EliteI";
    }
}