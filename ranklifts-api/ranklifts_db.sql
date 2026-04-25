-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 25, 2026 at 02:12 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ranklifts_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `exercises`
--

CREATE TABLE `exercises` (
  `exercise_id` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `movement_type` varchar(256) NOT NULL,
  `exercise_type` varchar(256) NOT NULL,
  `target_muscle_group` varchar(256) NOT NULL,
  `movementfactor` float NOT NULL,
  `isolationfactor` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `exercises`
--

INSERT INTO `exercises` (`exercise_id`, `name`, `movement_type`, `exercise_type`, `target_muscle_group`, `movementfactor`, `isolationfactor`) VALUES
(22, 'Barbell Bench Press', 'push', 'Compound', 'Chest', 0, 0),
(23, 'Barbell Overhead Press', 'push', 'Compound', 'Shoulders', 0, 0),
(24, 'Incline Dumbbell Press', 'push', 'Compound', 'Chest', 0, 0),
(25, 'Dumbbell Bench Press', 'push', 'Compound', 'Chest', 0, 0),
(26, 'Push-ups', 'push', 'Calisthenics', 'Chest', 0.65, 0),
(27, 'Dips', 'push', 'Calisthenics', 'Triceps', 0.85, 0),
(28, 'Pike Push-ups', 'push', 'Calisthenics', 'Shoulders', 0.6, 0),
(29, 'Tricep Pushdown', 'push', 'PrimaryIsolation', 'Triceps', 0, 0.2),
(30, 'Dumbbell Lateral Raise', 'push', 'PrimaryIsolation', 'Shoulders', 0, 0.15),
(31, 'Cable Chest Fly', 'push', 'PureIsolation', 'Chest', 0, 0.25),
(32, 'Barbell Row', 'pull', 'Compound', 'Back', 0, 0),
(33, 'Deadlift', 'pull', 'Compound', 'Back', 0, 0),
(34, 'Barbell Bent Over Row', 'pull', 'Compound', 'Back', 0, 0),
(35, 'Pull-ups', 'pull', 'Calisthenics', 'Back', 1, 0),
(36, 'Chin-ups', 'pull', 'Calisthenics', 'Biceps', 1, 0),
(37, 'Australian Pull-ups', 'pull', 'Calisthenics', 'Back', 0.6, 0),
(38, 'Bicep Curls', 'pull', 'PrimaryIsolation', 'Biceps', 0, 0.2),
(39, 'Lat Pulldown', 'pull', 'PrimaryIsolation', 'Back', 0, 0.35),
(40, 'Face Pulls', 'pull', 'PureIsolation', 'Rear Delts', 0, 0.15),
(41, 'Barbell Back Squat', 'legs', 'Compound', 'Quads', 0, 0),
(42, 'Barbell Front Squat', 'legs', 'Compound', 'Quads', 0, 0),
(43, 'Leg Press', 'legs', 'Compound', 'Quads', 0, 0),
(44, 'Bodyweight Squats', 'legs', 'Calisthenics', 'Quads', 0.85, 0),
(45, 'Pistol Squats', 'legs', 'Calisthenics', 'Quads', 0.9, 0),
(46, 'Lunges', 'legs', 'Calisthenics', 'Quads', 0.8, 0),
(47, 'Leg Extensions', 'legs', 'PrimaryIsolation', 'Quads', 0, 0.3),
(48, 'Hamstring Curls', 'legs', 'PrimaryIsolation', 'Hamstrings', 0, 0.25),
(49, 'Standing Calf Raises', 'legs', 'PureIsolation', 'Calves', 0, 0.4);

-- --------------------------------------------------------

--
-- Table structure for table `profiles`
--

CREATE TABLE `profiles` (
  `profile_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `bodyweight` int(11) NOT NULL CHECK (`bodyweight` between 0 and 300)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `profiles`
--

INSERT INTO `profiles` (`profile_id`, `user_id`, `bodyweight`) VALUES
(40, 10, 80);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(256) NOT NULL,
  `email` varchar(256) NOT NULL,
  `password_hash` varchar(256) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `password_hash`, `created_at`) VALUES
(10, 'test', 'test@gmail.com', '$2y$10$NeamS5MrXeDtqh1itVHaGuXsIS6Y39k1iMxODnzqExsvWKiKcgfLe', '2026-04-22 15:46:06'),
(11, 'test2', 'test2@gmail.com', '$2y$10$wEYk/v8iDIHZjAiLNS7TO.nLCsG5o8St.Y.vFjxs3In/9KGVIEz5u', '2026-04-24 08:17:45'),
(12, 'test3', 'test3@gmail.com', '$2y$10$6MmWFrmrjx6mDNhjyo04hO4NwfgYEhob7oEptV154akuaOjzU.yju', '2026-04-24 08:39:38'),
(13, 'test4', 'test4@gmail.com', '$2y$10$8zuoX5KR/jjGAI3nak9m.ezFB7uSdMjWa62m8zqmOeE4m/O6WOS/.', '2026-04-24 08:50:21');

-- --------------------------------------------------------

--
-- Table structure for table `user_category_rank`
--

CREATE TABLE `user_category_rank` (
  `user_category_rank_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `movement_type` varchar(256) NOT NULL,
  `category_score` float NOT NULL,
  `category_rank` varchar(50) NOT NULL,
  `calculated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_category_rank`
--

INSERT INTO `user_category_rank` (`user_category_rank_id`, `user_id`, `movement_type`, `category_score`, `category_rank`, `calculated_at`) VALUES
(64, 10, 'Push', 1.78571, 'DiamondI', '2026-04-24 15:56:58'),
(65, 10, 'Pull', 1.85, 'EliteIII', '2026-04-24 15:56:58'),
(66, 10, 'Legs', 1.5, 'DiamondIII', '2026-04-24 15:56:58');

-- --------------------------------------------------------

--
-- Table structure for table `user_exercise_performance`
--

CREATE TABLE `user_exercise_performance` (
  `performance_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `exercise_id` int(11) NOT NULL,
  `exercise_name` varchar(256) NOT NULL,
  `exercise_type` varchar(256) NOT NULL,
  `added_weight` float NOT NULL CHECK (`added_weight` between 0 and 750),
  `reps` int(11) NOT NULL,
  `estimated_1rm` float NOT NULL,
  `relative_strength` float NOT NULL,
  `exercise_rank` varchar(50) NOT NULL,
  `recorded_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_exercise_performance`
--

INSERT INTO `user_exercise_performance` (`performance_id`, `user_id`, `exercise_id`, `exercise_name`, `exercise_type`, `added_weight`, `reps`, `estimated_1rm`, `relative_strength`, `exercise_rank`, `recorded_at`) VALUES
(33, 10, 22, 'Barbell Bench Press', 'Compound', 100, 6, 120, 1.5, 'DiamondIII', '2026-04-24 15:56:20'),
(34, 10, 35, 'Pull-ups', 'Calisthenics', 35, 6, 138, 1.7, 'DiamondI', '2026-04-24 15:56:58'),
(35, 10, 41, 'Barbell Back Squat', 'Compound', 100, 6, 120, 1.5, 'DiamondIII', '2026-04-24 15:55:59'),
(36, 10, 26, 'Push-ups', 'Calisthenics', 100, 6, 182.4, 2.3, 'EliteI', '2026-04-24 15:56:20'),
(37, 10, 38, 'Bicep Curls', 'PrimaryIsolation', 30, 4, 34, 2.1, 'PlatinumI', '2026-04-24 15:56:58');

-- --------------------------------------------------------

--
-- Table structure for table `user_overall_rank`
--

CREATE TABLE `user_overall_rank` (
  `overall_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `overall_score` float NOT NULL,
  `rank` varchar(50) NOT NULL,
  `calculated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_overall_rank`
--

INSERT INTO `user_overall_rank` (`overall_id`, `user_id`, `overall_score`, `rank`, `calculated_at`) VALUES
(8, 10, 1.69478, 'PlatinumIII', '2026-04-24 15:56:58');

-- --------------------------------------------------------

--
-- Table structure for table `user_priorities`
--

CREATE TABLE `user_priorities` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `movement_type` varchar(50) NOT NULL,
  `first_priority` varchar(100) NOT NULL,
  `second_priority` varchar(100) NOT NULL,
  `third_priority` varchar(100) NOT NULL,
  `fourth_priority` varchar(100) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_priorities`
--

INSERT INTO `user_priorities` (`id`, `user_id`, `movement_type`, `first_priority`, `second_priority`, `third_priority`, `fourth_priority`, `updated_at`) VALUES
(2, 10, 'Push', 'Compound', 'Calisthenics', 'PrimaryIsolation', 'PureIsolation', '2026-04-24 10:24:16'),
(3, 10, 'Pull', 'Compound', 'Calisthenics', 'PrimaryIsolation', 'PureIsolation', '2026-04-24 10:24:16'),
(4, 10, 'Legs', 'Compound', 'Calisthenics', 'PrimaryIsolation', 'PureIsolation', '2026-04-24 10:24:16');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `exercises`
--
ALTER TABLE `exercises`
  ADD PRIMARY KEY (`exercise_id`);

--
-- Indexes for table `profiles`
--
ALTER TABLE `profiles`
  ADD PRIMARY KEY (`profile_id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `user_category_rank`
--
ALTER TABLE `user_category_rank`
  ADD PRIMARY KEY (`user_category_rank_id`),
  ADD UNIQUE KEY `unique_user_movement` (`user_id`,`movement_type`),
  ADD KEY `idx_user_id` (`user_id`);

--
-- Indexes for table `user_exercise_performance`
--
ALTER TABLE `user_exercise_performance`
  ADD PRIMARY KEY (`performance_id`),
  ADD KEY `exercise_id` (`exercise_id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_movement_type` (`exercise_type`);

--
-- Indexes for table `user_overall_rank`
--
ALTER TABLE `user_overall_rank`
  ADD PRIMARY KEY (`overall_id`),
  ADD UNIQUE KEY `user_id` (`user_id`),
  ADD KEY `idx_user_id` (`user_id`);

--
-- Indexes for table `user_priorities`
--
ALTER TABLE `user_priorities`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_user_movement` (`user_id`,`movement_type`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `exercises`
--
ALTER TABLE `exercises`
  MODIFY `exercise_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- AUTO_INCREMENT for table `profiles`
--
ALTER TABLE `profiles`
  MODIFY `profile_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `user_category_rank`
--
ALTER TABLE `user_category_rank`
  MODIFY `user_category_rank_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;

--
-- AUTO_INCREMENT for table `user_exercise_performance`
--
ALTER TABLE `user_exercise_performance`
  MODIFY `performance_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `user_overall_rank`
--
ALTER TABLE `user_overall_rank`
  MODIFY `overall_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `user_priorities`
--
ALTER TABLE `user_priorities`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `profiles`
--
ALTER TABLE `profiles`
  ADD CONSTRAINT `profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `user_category_rank`
--
ALTER TABLE `user_category_rank`
  ADD CONSTRAINT `user_category_rank_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `user_exercise_performance`
--
ALTER TABLE `user_exercise_performance`
  ADD CONSTRAINT `user_exercise_performance_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `user_exercise_performance_ibfk_2` FOREIGN KEY (`exercise_id`) REFERENCES `exercises` (`exercise_id`) ON DELETE CASCADE;

--
-- Constraints for table `user_overall_rank`
--
ALTER TABLE `user_overall_rank`
  ADD CONSTRAINT `user_overall_rank_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `user_priorities`
--
ALTER TABLE `user_priorities`
  ADD CONSTRAINT `user_priorities_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
