package com.exsoloscript.challonge.gson;

import com.exsoloscript.challonge.model.Attachment;
import com.exsoloscript.challonge.model.Match;
import com.exsoloscript.challonge.model.Participant;
import com.exsoloscript.challonge.model.Tournament;
import com.exsoloscript.challonge.model.query.MatchQuery;
import com.exsoloscript.challonge.model.query.ParticipantQuery;
import com.exsoloscript.challonge.model.query.TournamentQuery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collects the type adapters and creates a Gson object with them
 *
 * @author EXSolo
 * @version 20160819.1
 */
@Singleton
public class AdapterSuite implements Provider<Gson> {

    private GsonBuilder gsonBuilder;

    private Map<Type, GsonAdapter> typeMappings() {
        Map<Type, GsonAdapter> adapters = new HashMap<>();

        // pojo
        adapters.put(Tournament.class, new TournamentAdapter());
        adapters.put(Participant.class, new ParticipantAdapter());
        adapters.put(Match.class, new MatchAdapter());
        adapters.put(Attachment.class, new AttachmentAdapter());
        //query
        adapters.put(TournamentQuery.class, new TournamentQueryAdapter());
        adapters.put(MatchQuery.class, new MatchQueryAdapter());
        adapters.put(new TypeToken<List<ParticipantQuery>>() {
        }.getType(), new ParticipantQueryListAdapter());

        return adapters;
    }

    private GsonBuilder createGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();

        for (Map.Entry<Type, GsonAdapter> adapter : typeMappings().entrySet())
            builder.registerTypeAdapter(adapter.getKey(), adapter.getValue());

        return builder;
    }

    @Override
    public Gson get() {
        if (this.gsonBuilder == null) {
            this.gsonBuilder = createGsonBuilder();
        }

        return this.gsonBuilder.create();
    }
}
