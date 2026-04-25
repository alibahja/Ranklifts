package com.example.ranklifts_java.api;

import com.example.ranklifts_java.models.AuthRequest;
import com.example.ranklifts_java.models.AuthResponse;
import com.example.ranklifts_java.models.BatchUpdateRequest;
import com.example.ranklifts_java.models.CategoryRank;
import com.example.ranklifts_java.models.CategoryRankRequest;
import com.example.ranklifts_java.models.ExercisePerformance;
import com.example.ranklifts_java.models.ExercisePerformanceRequest;
import com.example.ranklifts_java.models.OverallRank;
import com.example.ranklifts_java.models.Profile;
import com.example.ranklifts_java.models.ProfileRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // ==================== AUTH ====================
    @POST("api/auth/register")
    Call<AuthResponse> register(@Body AuthRequest request);

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    // ==================== PROFILE ====================
    @GET("api/profile")
    Call<Profile> getProfile();

    @POST("api/profile")
    Call<Profile> createProfile(@Body ProfileRequest profileRequest);

    @PUT("api/profile")
    Call<Profile> updateProfile(@Body ProfileRequest profileRequest);

    // ==================== EXERCISE PERFORMANCE ====================
    @POST("api/exercise-performance")
    Call<ExercisePerformance> calculateExerciseRank(@Body ExercisePerformanceRequest request);

    @PUT("api/exercise-performance/{id}")
    Call<ExercisePerformance> updateExerciseRank(@Path("id") int id, @Body ExercisePerformanceRequest request);

    @GET("api/exercise-performance/{id}")
    Call<ExercisePerformance> getExerciseById(@Path("id") int id);

    @DELETE("api/exercise-performance/{id}")
    Call<ExercisePerformance> deleteExercise(@Path("id") int id);

    @GET("api/exercise-performance/push")
    Call<List<ExercisePerformance>> getPushPerformances();

    @GET("api/exercise-performance/pull")
    Call<List<ExercisePerformance>> getPullPerformances();

    @GET("api/exercise-performance/legs")
    Call<List<ExercisePerformance>> getLegsPerformances();

    @POST("api/exercise-performance/batch-update")
    Call<List<ExercisePerformance>> updateExerciseBatch(@Body List<BatchUpdateRequest> requests);

    // ==================== CATEGORY RANK ====================
    @POST("api/categoryrank")
    Call<CategoryRank> calculateCategoryRank(@Body CategoryRankRequest request);

    @PUT("api/categoryrank")
    Call<CategoryRank> updateCategoryRank(@Body CategoryRankRequest request);

    @GET("api/categoryrank/{movementType}")
    Call<CategoryRank> getCategoryRank(@Path("movementType") String movementType);

    // ==================== OVERALL RANK ====================
    @GET("api/overallrank")
    Call<OverallRank> getOverallRank();

    @POST("api/overallrank")
    Call<OverallRank> createOverallRank();

    @PUT("api/overallrank")
    Call<OverallRank> updateOverallRank();

    // ==================== RANKINGS ====================
    @POST("api/rankings/recalculate-all")
    Call<RecalculationResponse> recalculateAll();

    @POST("api/rankings/recalculate-all-profile")
    Call<RecalculationResponse> recalculateAllProfile();

    @GET("api/rankings/status")
    Call<RankingsStatus> getRankingsStatus();
}
