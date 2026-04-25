<?php

namespace Repositories;

use Config\Database;
use Models\UserPriority;
use PDO;

class UserPriorityRepo
{
    private PDO $db;
    
    public function __construct()
    {
        $this->db = Database::getInstance()->getConnection();
    }
    
    public function save(UserPriority $priority): void
    {  
         error_log("=== UserPriorityRepo::save CALLED ===");
    error_log("userId: " . $priority->getUserId());
    error_log("movementType: '" . $priority->getMovementType() . "'");
    error_log("firstPriority: '" . $priority->getFirstPriority() . "'");

        $sql = "INSERT INTO user_priorities (user_id, movement_type, first_priority, second_priority, third_priority, fourth_priority) 
                VALUES (:user_id, :movement_type, :first, :second, :third, :fourth)
                ON DUPLICATE KEY UPDATE 
                first_priority = VALUES(first_priority),
                second_priority = VALUES(second_priority),
                third_priority = VALUES(third_priority),
                fourth_priority = VALUES(fourth_priority)";

               
        
        $stmt = $this->db->prepare($sql);
         
        $stmt->execute([
            ':user_id' => $priority->getUserId(),
            ':movement_type' => $priority->getMovementType(),
            ':first' => $priority->getFirstPriority(),
            ':second' => $priority->getSecondPriority(),
            ':third' => $priority->getThirdPriority(),
            ':fourth' => $priority->getFourthPriority()
        ]);
         error_log("SQL executed, rowCount: " . $stmt->rowCount());
    }
    
    public function getByUserAndMovement(int $userId, string $movementType): ?UserPriority
    {
        $sql = "SELECT * FROM user_priorities WHERE user_id = :user_id AND movement_type = :movement_type";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':user_id' => $userId,
            ':movement_type' => $movementType
        ]);
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        return $data ? UserPriority::fromArray($data) : null;
    }
}