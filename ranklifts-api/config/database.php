<?php

namespace Config;

use PDO;
use PDOException;

class Database
{
    private static ?Database $instance = null;
    private ?PDO $connection = null;
    
    // Database credentials for XAMPP
    private string $host = 'localhost';
    private string $dbname = 'ranklifts_db';
    private string $username = 'root';
    private string $password = '';
    private string $charset = 'utf8mb4';
    
    private function __construct()
    {
        $this->connect();
    }
    
    // Singleton pattern
    public static function getInstance(): Database
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }
    
    private function connect(): void
    {
        try {
            $dsn = "mysql:host={$this->host};dbname={$this->dbname};charset={$this->charset}";
            
            $this->connection = new PDO($dsn, $this->username, $this->password);
            
            // Set PDO attributes
            $this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->connection->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
            $this->connection->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
            
        } catch (PDOException $e) {
            die("Database connection failed: " . $e->getMessage());
        }
    }
    
    public function getConnection(): PDO
    {
        if ($this->connection === null) {
            $this->connect();
        }
        return $this->connection;
    }
    
    // Begin transaction
    public function beginTransaction(): bool
    {
        return $this->connection->beginTransaction();
    }
    
    // Commit transaction
    public function commit(): bool
    {
        return $this->connection->commit();
    }
    
    // Rollback transaction
    public function rollback(): bool
    {
        return $this->connection->rollBack();
    }
    
    // Prevent cloning
    private function __clone() {}
    
    // Prevent unserialization
    public function __wakeup() {}
}