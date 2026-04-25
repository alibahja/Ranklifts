<?php

namespace Models;

class Exercise
{
    private ?int $exerciseId;
    private string $name;
    private string $movementType;
    private string $exerciseType;
    private string $targetMuscleGroup;
    private float $movementFactor;
    private float $isolationFactor;
    
    public function __construct()
    {
        $this->exerciseId = null;
    }
    
    // Getters
    public function getExerciseId(): ?int
    {
        return $this->exerciseId;
    }
    
    public function getName(): string
    {
        return $this->name;
    }
    
    public function getMovementType(): string
    {
        return $this->movementType;
    }
    
    public function getExerciseType(): string
    {
        return $this->exerciseType;
    }
    
    public function getTargetMuscleGroup(): string
    {
        return $this->targetMuscleGroup;
    }
    
    public function getMovementFactor(): float
    {
        return $this->movementFactor;
    }
    
    public function getIsolationFactor(): float
    {
        return $this->isolationFactor;
    }
    
    // Setters
    public function setExerciseId(?int $exerciseId): void
    {
        $this->exerciseId = $exerciseId;
    }
    
    public function setName(string $name): void
    {
        $this->name = $name;
    }
    
    public function setMovementType(string $movementType): void
    {
        $this->movementType = $movementType;
    }
    
    public function setExerciseType(string $exerciseType): void
    {
        $this->exerciseType = $exerciseType;
    }
    
    public function setTargetMuscleGroup(string $targetMuscleGroup): void
    {
        $this->targetMuscleGroup = $targetMuscleGroup;
    }
    
    public function setMovementFactor(float $movementFactor): void
    {
        $this->movementFactor = $movementFactor;
    }
    
    public function setIsolationFactor(float $isolationFactor): void
    {
        $this->isolationFactor = $isolationFactor;
    }
    
    // Convert to array
    public function toArray(): array
    {
        return [
            'exerciseId' => $this->exerciseId,
            'name' => $this->name,
            'movementType' => $this->movementType,
            'exerciseType' => $this->exerciseType,
            'targetMuscleGroup' => $this->targetMuscleGroup,
            'movementFactor' => $this->movementFactor,
            'isolationFactor' => $this->isolationFactor
        ];
    }
    
    // Create from database row
    public static function fromArray(array $data): Exercise
    {
        $exercise = new Exercise();
        $exercise->setExerciseId($data['exercise_id'] ?? null);
        $exercise->setName($data['name'] ?? '');
        $exercise->setMovementType($data['movement_type'] ?? '');
        $exercise->setExerciseType($data['exercise_type'] ?? '');
        $exercise->setTargetMuscleGroup($data['target_muscle_group'] ?? '');
        $exercise->setMovementFactor((float)($data['movementfactor'] ?? 0));
        $exercise->setIsolationFactor((float)($data['isolationfactor'] ?? 0));
        
        return $exercise;
    }
}