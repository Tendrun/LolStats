package com.backendwebsite.DatabaseBuilder.DTO.AppApi.Match;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;

public record GetMatchesRequest(String region, int playerLimit, FetchMatchesContext.Type type) { }
