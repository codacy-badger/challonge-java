package com.exsoloscript.challonge.handler;

import com.exsoloscript.challonge.model.Tournament;
import com.exsoloscript.challonge.model.Tournament.TournamentState;
import com.exsoloscript.challonge.model.TournamentBase;
import com.exsoloscript.challonge.model.TournamentBase.TournamentType;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface TournamentHandler extends ApiHandler {

    @GET("/tournaments.json")
    Call<List<Tournament>> getTournaments(@Query("state") TournamentState state,
                                          @Query("type") TournamentType type,
                                          @Query("created_after") String createdAfter,
                                          @Query("created_before") String createdBefore,
                                          @Query("subdomain") String subdomain
    );

    @GET("/tournaments/{tournament}.json")
    Call<Tournament> getTournament(@Path("tournament") String name, @Query("include_participants") boolean includeParticipants, @Query("include_matches") boolean includeMatches);

    @POST("/tournaments.json")
    Call<Tournament> createTournament(@Body TournamentBase tournament);

    @PUT("/tournaments/{tournament}.json")
    Call<Tournament> updateTournament(@Path("tournament") String name, @Body TournamentBase tournament);

    @DELETE("/tournaments/{tournament}.json")
    Call<Tournament> deleteTournament(@Path("tournament") String name);

    @POST("/tournaments/{tournament}/process_check_ins.json")
    Call<Tournament> processCheckIns(@Path("tournament") String name, @Query("include_participants") boolean includeParticipants, @Query("include_matches") boolean includeMatches);

    @POST("/tournaments/{tournament}/abort_check_in.json")
    Call<Tournament> abortCheckIn(@Path("tournament") String name, @Query("include_participants") boolean includeParticipants, @Query("include_matches") boolean includeMatches);

    @POST("/tournaments/{tournament}/start.json")
    Call<Tournament> startTournament(@Path("tournament") String name, @Query("include_participants") boolean includeParticipants, @Query("include_matches") boolean includeMatches);

    @POST("/tournaments/{tournament}/finalize.json")
    Call<Tournament> finalizeTournament(@Path("tournament") String name, @Query("include_participants") boolean includeParticipants, @Query("include_matches") boolean includeMatches);

    @POST("/tournaments/{tournament}/reset.json")
    Call<Tournament> resetTournament(@Path("tournament") String name, @Query("include_participants") boolean includeParticipants, @Query("include_matches") boolean includeMatches);
}