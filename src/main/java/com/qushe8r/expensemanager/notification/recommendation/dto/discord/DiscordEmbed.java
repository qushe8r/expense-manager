package com.qushe8r.expensemanager.notification.recommendation.dto.discord;

import java.util.List;

public record DiscordEmbed(
    DiscordEmbedAuthor author,
    DiscordEmbedThumbnail thumbnail,
    Integer color,
    List<DiscordEmbedField> fields,
    DiscordEmbedFooter footer) {}
