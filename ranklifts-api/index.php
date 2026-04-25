<?php
require_once __DIR__ . '/vendor/autoload.php';

// Enable error reporting for development
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Set CORS headers
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With");
header("Content-Type: application/json; charset=UTF-8");

// Handle preflight OPTIONS request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Autoloader
spl_autoload_register(function ($class) {
    $prefix = '';
    $base_dir = __DIR__ . '/';
    
    $file = $base_dir . str_replace('\\', '/', $class) . '.php';
    
    if (file_exists($file)) {
        require $file;
    }
});

// Get request URI and method
$requestUri = $_SERVER['REQUEST_URI'];
$requestMethod = $_SERVER['REQUEST_METHOD'];

// Remove query string if present
if (strpos($requestUri, '?') !== false) {
    $requestUri = substr($requestUri, 0, strpos($requestUri, '?'));
}

// Remove base path (adjust if your app is in a subfolder)
// For example, if your app is at http://localhost/ranklifts-api/, uncomment below:
$basePath = '/ranklifts-api';
$requestUri = str_replace($basePath, '', $requestUri);

// Parse route parameters
$routeParams = [];
$routePath = trim($requestUri, '/');

// Router
try {
    // ==================== AUTH ROUTES (Public) ====================
    if ($routePath === 'api/auth/register' && $requestMethod === 'POST') {
        $controller = new \Controllers\AuthController();
        $controller->register();
    }
    else if ($routePath === 'api/auth/login' && $requestMethod === 'POST') {
        $controller = new \Controllers\AuthController();
        $controller->login();
    }
    
    // ==================== PROFILE ROUTES (Auth required) ====================
    else if ($routePath === 'api/profile' && $requestMethod === 'GET') {
        $controller = new \Controllers\ProfileController();
        $controller->getProfile();
    }
    else if ($routePath === 'api/profile' && $requestMethod === 'POST') {
        $controller = new \Controllers\ProfileController();
        $controller->createProfile();
    }
    else if ($routePath === 'api/profile' && $requestMethod === 'PUT') {
        $controller = new \Controllers\ProfileController();
        $controller->updateProfile();
    }
    
    // ==================== EXERCISE PERFORMANCE ROUTES (Auth required) ====================
    else if ($routePath === 'api/exercise-performance' && $requestMethod === 'POST') {
        $controller = new \Controllers\ExercisePerformanceController();
        $controller->calculateExerciseRank();
    }
    else if (preg_match('/^api\/exercise-performance\/(\d+)$/', $routePath, $matches) && $requestMethod === 'PUT') {
        $controller = new \Controllers\ExercisePerformanceController();
        $controller->updateExerciseRank($matches[1]);
    }
    else if (preg_match('/^api\/exercise-performance\/(\d+)$/', $routePath, $matches) && $requestMethod === 'GET') {
        $controller = new \Controllers\ExercisePerformanceController();
        $controller->getExercisePerformance($matches[1]);
    }
    else if (preg_match('/^api\/exercise-performance\/(\d+)$/', $routePath, $matches) && $requestMethod === 'DELETE') {
        $controller = new \Controllers\ExercisePerformanceController();
        $controller->deleteExercisePerformance($matches[1]);
    }
    else if ($routePath === 'api/exercise-performance/batch-update' && $requestMethod === 'POST') {
        $controller = new \Controllers\ExercisePerformanceController();
        $controller->updateExerciseBatch();
    }
    else if ($routePath === 'api/exercise-performance/pull' && $requestMethod === 'GET') {
        $controller = new \Controllers\ExercisePerformanceController();
        $controller->getPullPerformance();
    }
    else if ($routePath === 'api/exercise-performance/push' && $requestMethod === 'GET') {
        $controller = new \Controllers\ExercisePerformanceController();
        $controller->getPushPerformance();
    }
    else if ($routePath === 'api/exercise-performance/legs' && $requestMethod === 'GET') {
        $controller = new \Controllers\ExercisePerformanceController();
        $controller->getLegsPerformance();
    }
    // Add this line
else if ($routePath === 'api/exercise-performance/batch-update' && $requestMethod === 'POST') {
    $controller = new \Controllers\ExercisePerformanceController();
    $controller->updateExerciseBatch(); // We will create this method next
}
    
    // ==================== CATEGORY RANK ROUTES (Auth required) ====================
    else if ($routePath === 'api/categoryrank' && $requestMethod === 'POST') {
        $controller = new \Controllers\CategoryRankController();
        $controller->calculateCategoryRank();
    }
    else if ($routePath === 'api/categoryrank' && $requestMethod === 'PUT') {
        $controller = new \Controllers\CategoryRankController();
        $controller->updateCategoryRank();
    }
    else if (preg_match('/^api\/categoryrank\/(.+)$/', $routePath, $matches) && $requestMethod === 'GET') {
        $controller = new \Controllers\CategoryRankController();
        $controller->getCategoryRank($matches[1]);
    }
    
    // ==================== OVERALL RANK ROUTES (Auth required) ====================
    else if ($routePath === 'api/overallrank' && $requestMethod === 'GET') {
        $controller = new \Controllers\OverallRankController();
        $controller->getOverallRank();
    }
    else if ($routePath === 'api/overallrank' && $requestMethod === 'POST') {
        $controller = new \Controllers\OverallRankController();
        $controller->createOverallRank();
    }
    else if ($routePath === 'api/overallrank' && $requestMethod === 'PUT') {
        $controller = new \Controllers\OverallRankController();
        $controller->updateOverallRank();
    }
    
    // ==================== RANKINGS ROUTES (Auth required) ====================
    else if ($routePath === 'api/rankings/recalculate-all' && $requestMethod === 'POST') {
        $controller = new \Controllers\RankingsController();
        $controller->recalculateAll();
    }
    else if ($routePath === 'api/rankings/recalculate-all-profile' && $requestMethod === 'POST') {
        $controller = new \Controllers\RankingsController();
        $controller->recalculateAllProfile();
    }
    else if ($routePath === 'api/rankings/status' && $requestMethod === 'GET') {
        $controller = new \Controllers\RankingsController();
        $controller->getRankingsStatus();
    }
    
    // ==================== 404 Not Found ====================
    else {
        http_response_code(404);
        echo json_encode([
            'error' => 'Endpoint not found',
            'path' => $routePath,
            'method' => $requestMethod,
            'timestamp' => date('Y-m-d H:i:s')
        ]);
    }
    
} catch (\Exception $e) {
    http_response_code(500);
    echo json_encode([
        'error' => 'Server error: ' . $e->getMessage(),
        'timestamp' => date('Y-m-d H:i:s')
    ]);
}