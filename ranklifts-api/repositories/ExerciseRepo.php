<?php

namespace Repositories;

use Config\Database;
use Models\Exercise;
use PDO;

class ExerciseRepo
{
    private PDO $db;
    
    public function __construct()
    {
        $this->db = Database::getInstance()->getConnection();
    }
    
    /**
     * Get exercise by ID
     */
    public function getByExerciseId(int $id): ?Exercise
    {
        $sql = "SELECT * FROM exercises WHERE exercise_id = :id";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':id' => $id]);
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$data) {
            return null;
        }
        
        return Exercise::fromArray($data);
    }
    
    /**
     * Get exercise by name
     */
    public function getByExerciseName(string $name): ?Exercise
    {
        $sql = "SELECT * FROM exercises WHERE name = :name";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':name' => $name]);
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$data) {
            return null;
        }
        
        return Exercise::fromArray($data);
    }
    
    /**
     * Get exercise by movement type (returns first match)
     */
    public function getByMovementType(string $movementType): ?Exercise
    {
        $sql = "SELECT * FROM exercises WHERE movement_type = :movement_type LIMIT 1";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':movement_type' => $movementType]);
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$data) {
            return null;
        }
        
        return Exercise::fromArray($data);
    }
    
    /**
     * Get exercise by exercise type (returns first match)
     */
    public function getByExerciseType(string $exerciseType): ?Exercise
    {
        $sql = "SELECT * FROM exercises WHERE exercise_type = :exercise_type LIMIT 1";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':exercise_type' => $exerciseType]);
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$data) {
            return null;
        }
        
        return Exercise::fromArray($data);
    }
    
    /**
     * Get all exercises by movement type
     */
    public function getAllByMovementType(string $movementType): array
    {
        $sql = "SELECT * FROM exercises WHERE movement_type = :movement_type";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':movement_type' => $movementType]);
        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        $exercises = [];
        foreach ($data as $row) {
            $exercises[] = Exercise::fromArray($row);
        }
        
        return $exercises;
    }
    
    /**
     * Get all exercises
     */
    public function getAll(): array
    {
        $sql = "SELECT * FROM exercises";
        $stmt = $this->db->query($sql);
        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        $exercises = [];
        foreach ($data as $row) {
            $exercises[] = Exercise::fromArray($row);
        }
        
        return $exercises;
    }
}