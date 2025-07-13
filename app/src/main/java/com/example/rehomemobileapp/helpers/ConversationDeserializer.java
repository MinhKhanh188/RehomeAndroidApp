package com.example.rehomemobileapp.helpers;

import com.example.rehomemobileapp.model.Conversation;
import com.example.rehomemobileapp.model.Participant;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationDeserializer implements JsonDeserializer<Conversation> {
    @Override
    public Conversation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        Conversation conversation = new Conversation();
        conversation.setId(obj.get("_id").getAsString());

        // CreatedAt
        if (obj.has("createdAt") && !obj.get("createdAt").isJsonNull()) {
            conversation.setCreatedAt(context.deserialize(obj.get("createdAt"), Date.class));
        }

        // Handle participants
        JsonArray participantArray = obj.get("participants").getAsJsonArray();
        List<Participant> participantList = new ArrayList<>();

        for (JsonElement elem : participantArray) {
            if (elem.isJsonPrimitive()) {
                // Case: String ID only
                String id = elem.getAsString();
                Participant p = new Participant();
                p.set_id(id);
                participantList.add(p);
            } else if (elem.isJsonObject()) {
                // Full Participant object
                Participant p = context.deserialize(elem, Participant.class);
                participantList.add(p);
            }
        }
        conversation.setParticipants(participantList);

        // lastMessage (if exists)
        if (obj.has("lastMessage") && !obj.get("lastMessage").isJsonNull()) {
            conversation.setLastMessage(context.deserialize(obj.get("lastMessage"), com.example.rehomemobileapp.model.Message.class));
        }

        return conversation;
    }
}
