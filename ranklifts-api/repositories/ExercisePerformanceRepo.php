<?php

namespace Repositories;

use Config\Database;
use Models\UserExercisePerformance;
use PDO;

class ExercisePerformanceRepo
{
    private PDO $db;
    
    public function __construct()
    {
        $this->db = Database::getInstance()->getConnection();
    }
    
    /**
     * Create exercise performance
     */
    public function create(UserExercisePerformance $performance): void
    {
        $sql = "INSERT INTO user_exercise_performance 
                (user_id, exercise_id, exercise_name, exercise_type, added_weight, reps, 
                 estimated_1rm, relative_strength, exercise_rank, recorded_at) 
                VALUES 
                (:user_id, :exercise_id, :exercise_name, :exercise_type, :added_weight, :reps,
                 :estimated_1rm, :relative_strength, :exercise_rank, :recorded_at)";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':user_id' => $performance->getUserId(),
            ':exercise_id' => $performance->getExerciseId(),
            ':exercise_name' => $performance->getExerciseName(),
            ':exercise_type' => $performance->getExerciseType(),
            ':added_weight' => $performance->getAddedWeight(),
            ':reps' => $performance->getReps(),
            ':estimated_1rm' => $performance->getEstimated1RM(),
            ':relative_strength' => $performance->getRelativeStrength(),
            ':exercise_rank' => $performance->getExerciseRank(),
            ':recorded_at' => $performance->getRecordedAt()
        ]);
        
        $performance->setPerformanceId((int)$this->db->lastInsertId());
    }
    
    /**
     * Delete exercise performance by ID
     */
    public function delete(int $id): void
    {
        $sql = "DELETE FROM user_exercise_performance WHERE performance_id = :id";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':id' => $id]);
    }
    
    /**
     * Get by performance ID
     */
    public function getByPerformanceId(int $id): ?UserExercisePerformance
    {
        $sql = "SELECT * FROM user_exercise_performance WHERE performance_id = :id";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':id' => $id]);
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$data) {
            return null;
        }
        
        return UserExercisePerformance::fromArray($data);
    }
    
    /**
     * Get by exercise ID (returns first match)
     */
    public function getByExerciseId(int $exerciseId): ?UserExercisePerformance
    {
        $sql = "SELECT * FROM user_exercise_performance WHERE exercise_id = :exercise_id LIMIT 1";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':exercise_id' => $exerciseId]);
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$data) {
            return null;
        }
        
        return UserExercisePerformance::fromArray($data);
    }
    
    /**
     * Get by exercise type and exercise ID
     */
    public function getByExerciseType(int $exerciseId, string $exerciseType): ?UserExercisePerformance
    {
        $sql = "SELECT * FROM user_exercise_performance 
                WHERE exercise_id = :exercise_id AND exercise_type = :exercise_type LIMIT 1";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':exercise_id' => $exerciseId,
            ':exercise_type' => $exerciseType
        ]);
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$data) {
            return null;
        }
        
        return UserExercisePerformance::fromArray($data);
    }
    
    /**
     * Get by user ID (returns first match)
     */
    public function getByUserId(int $userId): ?UserExercisePerformance
    {
        $sql = "SELECT * FROM user_exercise_performance WHERE user_id = :user_id LIMIT 1";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':user_id' => $userId]);
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$data) {
            return null;
        }
        
        return UserExercisePerformance::fromArray($data);
    }
    
    /**
     * Update exercise performance
     */
    public function update(UserExercisePerformance $performance): void
    {
        $sql = "UPDATE user_exercise_performance 
                SET user_id = :user_id,
                    exercise_id = :exercise_id,
                    exercise_name = :exercise_name,
                    exercise_type = :exercise_type,
                    added_weight = :added_weight,
                    reps = :reps,
                    estimated_1rm = :estimated_1rm,
                    relative_strength = :relative_strength,
                    exercise_rank = :exercise_rank,
                    recorded_at = :recorded_at
                WHERE performance_id = :performance_id";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':user_id' => $performance->getUserId(),
            ':exercise_id' => $performance->getExerciseId(),
            ':exercise_name' => $performance->getExerciseName(),
            ':exercise_type' => $performance->getExerciseType(),
            ':added_weight' => $performance->getAddedWeight(),
            ':reps' => $performance->getReps(),
            ':estimated_1rm' => $performance->getEstimated1RM(),
            ':relative_strength' => $performance->getRelativeStrength(),
            ':exercise_rank' => $performance->getExerciseRank(),
            ':recorded_at' => $performance->getRecordedAt(),
            ':performance_id' => $performance->getPerformanceId()
        ]);
    }
    
    /**
     * Get all pull performances for a user (joins exercises table)
     */
    public function getPullPerformance(int $userId): array
    {
        $sql = "SELECT p.* FROM user_exercise_performance p
                INNER JOIN exercises e ON p.exercise_id = e.exercise_id
                WHERE p.user_id = :user_id AND e.movement_type = 'Pull'
                ORDER BY p.recorded_at DESC";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':user_id' => $userId]);
        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        $performances = [];
        foreach ($data as $row) {
            $performances[] = UserExercisePerformance::fromArray($row);
        }
        
        return $performances;
    }
    
    /**
     * Get all push performances for a user (joins exercises table)
     */
    public function getPushPerformance(int $userId): array
    {
        $sql = "SELECT p.* FROM user_exercise_performance p
                INNER JOIN exercises e ON p.exercise_id = e.exercise_id
                WHERE p.user_id = :user_id AND e.movement_type = 'Push'
                ORDER BY p.recorded_at DESC";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':user_id' => $userId]);
        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        $performances = [];
        foreach ($data as $row) {
            $performances[] = UserExercisePerformance::fromArray($row);
        }
        
        return $performances;
    }
    
    /**
     * Get all legs performances for a user (joins exercises table)
     */
    public function getLegsPerformance(int $userId): array
    {
        $sql = "SELECT p.* FROM user_exercise_performance p
                INNER JOIN exercises e ON p.exercise_id = e.exercise_id
                WHERE p.user_id = :user_id AND e.movement_type = 'Legs'
                ORDER BY p.recorded_at DESC";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':user_id' => $userId]);
        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        $performances = [];
        foreach ($data as $row) {
            $performances[] = UserExercisePerformance::fromArray($row);
        }
        
        return $performances;
    }
    
    /**
     * Get all performances for a user
     */
    public function getAllByUserId(int $userId): array
    {
        $sql = "SELECT * FROM user_exercise_performance WHERE user_id = :user_id ORDER BY recorded_at DESC";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':user_id' => $userId]);
        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        $performances = [];
        foreach ($data as $row) {
            $performances[] = UserExercisePerformance::fromArray($row);
        }
        
        return $performances;
    }
}