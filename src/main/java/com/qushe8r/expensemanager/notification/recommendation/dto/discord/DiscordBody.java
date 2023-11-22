package com.qushe8r.expensemanager.notification.recommendation.dto.discord;

import java.util.List;

public record DiscordBody(
    String username, String avatar_url, String content, List<DiscordEmbed> embeds) {}
