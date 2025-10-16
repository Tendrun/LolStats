package com.backendwebsite.DatabaseBuilder.DTO.AppApi.MatchDetails;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;

public record GetMatchDetailsRequest(int playerMatchLimit, FetchMatchDetailsContext.Region region) { }
