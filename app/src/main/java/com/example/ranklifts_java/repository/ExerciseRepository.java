package com.example.ranklifts_java.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseRepository {

    public static class Exercise {
        private String id;
        private String name;
        private String movementType;
        private boolean isBodyweight;

        public Exercise(String id, String name, String movementType, boolean isBodyweight) {
            this.id = id;
            this.name = name;
            this.movementType = movementType;
            this.isBodyweight = isBodyweight;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getMovementType() { return movementType; }
        public boolean isBodyweight() { return isBodyweight; }
    }

    private static Map<String, List<Exercise>> exercisesByType = new HashMap<>();

    static {
        // Initialize exercises (from your exercises.ts file)
        List<Exercise> pushExercises = new ArrayList<>();
        List<Exercise> pullExercises = new ArrayList<>();
        List<Exercise> legsExercises = new ArrayList<>();

        // Push exercises - Compound
        pushExercises.add(new Exercise("barbell-bench-press", "Barbell Bench Press", "push", false));
        pushExercises.add(new Exercise("barbell-overhead-press", "Barbell Overhead Press", "push", false));
        pushExercises.add(new Exercise("incline-dumbbell-press", "Incline Dumbbell Press", "push", false));
        pushExercises.add(new Exercise("dumbbell-bench-press", "Dumbbell Bench Press", "push", false));

        // Push exercises - Calisthenics
        pushExercises.add(new Exercise("push-ups", "Push-ups", "push", true));
        pushExercises.add(new Exercise("dips", "Dips", "push", true));
        pushExercises.add(new Exercise("pike-push-ups", "Pike Push-ups", "push", true));

        // Push exercises - Isolation
        pushExercises.add(new Exercise("tricep-pushdown", "Tricep Pushdown", "push", false));
        pushExercises.add(new Exercise("dumbbell-lateral-raise", "Dumbbell Lateral Raise", "push", false));
        pushExercises.add(new Exercise("cable-chest-fly", "Cable Chest Fly", "push", false));

        // Pull exercises - Compound
        pullExercises.add(new Exercise("pull-ups", "Pull-ups", "pull", true));
        pullExercises.add(new Exercise("barbell-row", "Barbell Row", "pull", false));
        pullExercises.add(new Exercise("deadlift", "Deadlift", "pull", false));
        pullExercises.add(new Exercise("barbell-bent-over-row", "Barbell Bent Over Row", "pull", false));

        // Pull exercises - Calisthenics
        pullExercises.add(new Exercise("chin-ups", "Chin-ups", "pull", true));
        pullExercises.add(new Exercise("australian-pull-ups", "Australian Pull-ups", "pull", true));

        // Pull exercises - Isolation
        pullExercises.add(new Exercise("bicep-curls", "Bicep Curls", "pull", false));
        pullExercises.add(new Exercise("lat-pulldown", "Lat Pulldown", "pull", false));
        pullExercises.add(new Exercise("face-pulls", "Face Pulls", "pull", false));

        // Legs exercises - Compound
        legsExercises.add(new Exercise("barbell-back-squat", "Barbell Back Squat", "legs", false));
        legsExercises.add(new Exercise("deadlift", "Deadlift", "legs", false));
        legsExercises.add(new Exercise("barbell-front-squat", "Barbell Front Squat", "legs", false));
        legsExercises.add(new Exercise("leg-press", "Leg Press", "legs", false));

        // Legs exercises - Calisthenics
        legsExercises.add(new Exercise("bodyweight-squats", "Bodyweight Squats", "legs", true));
        legsExercises.add(new Exercise("pistol-squats", "Pistol Squats", "legs", true));
        legsExercises.add(new Exercise("lunges", "Lunges", "legs", true));

        // Legs exercises - Isolation
        legsExercises.add(new Exercise("leg-extensions", "Leg Extensions", "legs", false));
        legsExercises.add(new Exercise("hamstring-curls", "Hamstring Curls", "legs", false));
        legsExercises.add(new Exercise("standing-calf-raises", "Standing Calf Raises", "legs", false));

        exercisesByType.put("push", pushExercises);
        exercisesByType.put("pull", pullExercises);
        exercisesByType.put("legs", legsExercises);
    }

    public static List<Exercise> getExercisesByType(String movementType) {
        List<Exercise> exercises = exercisesByType.get(movementType);
        return exercises != null ? new ArrayList<>(exercises) : new ArrayList<>();
    }

    public static Exercise getExerciseById(String id) {
        for (List<Exercise> list : exercisesByType.values()) {
            for (Exercise exercise : list) {
                if (exercise.getId().equals(id)) {
                    return exercise;
                }
            }
        }
        return null;
    }

    public static String[] getExerciseNamesByType(String movementType) {
        List<Exercise> exercises = getExercisesByType(movementType);
        String[] names = new String[exercises.size()];
        for (int i = 0; i < exercises.size(); i++) {
            names[i] = exercises.get(i).getName();
        }
        return names;
    }

    public static Exercise getExerciseByName(String name) {
        for (List<Exercise> list : exercisesByType.values()) {
            for (Exercise exercise : list) {
                if (exercise.getName().equals(name)) {
                    return exercise;
                }
            }
        }
        return null;
    }
}