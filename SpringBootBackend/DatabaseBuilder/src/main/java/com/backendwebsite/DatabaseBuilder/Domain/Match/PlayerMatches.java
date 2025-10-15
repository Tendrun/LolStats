package com.backendwebsite.DatabaseBuilder.Domain.Match;

import java.util.List;

public record PlayerMatches(List<String> matchIds, String puuid, String _id, String _rev) { }