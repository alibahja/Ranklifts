<?php

namespace Models;

class UserExercisePerformance
{
    private ?int $performanceId;
    private int $userId;
    private int $exerciseId;
    private string $exerciseName;
    private string $exerciseType;
    private float $addedWeight;
    private int $reps;
    private float $estimated1RM;
    private float $relativeStrength;
    private string $exerciseRank;
    private string $recordedAt;
    
    public function __construct()
    {
        $this->performanceId = null;
        $this->recordedAt = date('Y-m-d H:i:s');
    }
    
    // Getters
    public function getPerformanceId(): ?int
    {
        return $this->performanceId;
    }
    
    public function getUserId(): int
    {
        return $this->userId;
    }
    
    public function getExerciseId(): int
    {
        return $this->exerciseId;
    }
    
    public function getExerciseName(): string
    {
        return $this->exerciseName;
    }
    
    public function getExerciseType(): string
    {
        return $this->exerciseType;
    }
    
    public function getAddedWeight(): float
    {
        return $this->addedWeight;
    }
    
    public function getReps(): int
    {
        return $this->reps;
    }
    
    public function getEstimated1RM(): float
    {
        return $this->estimated1RM;
    }
    
    public function getRelativeStrength(): float
    {
        return $this->relativeStrength;
    }
    
    public function getExerciseRank(): string
    {
        return $this->exerciseRank;
    }
    
    public function getRecordedAt(): string
    {
        return $this->recordedAt;
    }
    
    // Setters
    public function setPerformanceId(?int $performanceId): void
    {
        $this->performanceId = $performanceId;
    }
    
    public function setUserId(int $userId): void
    {
        $this->userId = $userId;
    }
    
    public function setExerciseId(int $exerciseId): void
    {
        $this->exerciseId = $exerciseId;
    }
    
    public function setExerciseName(string $exerciseName): void
    {
        $this->exerciseName = $exerciseName;
    }
    
    public function setExerciseType(string $exerciseType): void
    {
        $this->exerciseType = $exerciseType;
    }
    
    public function setAddedWeight(float $addedWeight): void
    {
        // Validate range 0-750 (same as ASP.NET)
        if ($addedWeight < 0 || $addedWeight > 750) {
            throw new \InvalidArgumentException("Added weight must be between 0 and 750");
        }
        $this->addedWeight = $addedWeight;
    }
    
    public function setReps(int $reps): void
    {
        $this->reps = $reps;
    }
    
    public function setEstimated1RM(float $estimated1RM): void
    {
        $this->estimated1RM = $estimated1RM;
    }
    
    public function setRelativeStrength(float $relativeStrength): void
    {
        $this->relativeStrength = $relativeStrength;
    }
    
    public function setExerciseRank(string $exerciseRank): void
    {
        $this->exerciseRank = $exerciseRank;
    }
    
    public function setRecordedAt(string $recordedAt): void
    {
        $this->recordedAt = $recordedAt;
    }
    
    // Convert to array
    public function toArray(): array
    {
        return [
            'performanceId' => $this->performanceId,
            'userId' => $this->userId,
            'exerciseId' => $this->exerciseId,
            'exerciseName' => $this->exerciseName,
            'exerciseType' => $this->exerciseType,
            'addedWeight' => $this->addedWeight,
            'reps' => $this->reps,
            'estimated1RM' => $this->estimated1RM,
            'relativeStrength' => $this->relativeStrength,
            'exerciseRank' => $this->exerciseRank,
            'recordedAt' => $this->recordedAt
        ];
    }
    
    // Create from database row
    public static function fromArray(array $data): UserExercisePerformance
    {
        $performance = new UserExercisePerformance();
        $performance->setPerformanceId($data['performance_id'] ?? null);
        $performance->setUserId($data['user_id'] ?? 0);
        $performance->setExerciseId($data['exercise_id'] ?? 0);
        $performance->setExerciseName($data['exercise_name'] ?? '');
        $performance->setExerciseType($data['exercise_type'] ?? '');
        $performance->setAddedWeight((float)($data['added_weight'] ?? 0));
        $performance->setReps((int)($data['reps'] ?? 0));
        $performance->setEstimated1RM((float)($data['estimated_1rm'] ?? 0));
        $performance->setRelativeStrength((float)($data['relative_strength'] ?? 0));
        $performance->setExerciseRank($data['exercise_rank'] ?? '');
        $performance->setRecordedAt($data['recorded_at'] ?? date('Y-m-d H:i:s'));
        
        return $performance;
    }
}