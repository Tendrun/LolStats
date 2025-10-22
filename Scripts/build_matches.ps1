$URL = "http://localhost:4020/api/database/FetchMatches"
$region = "europe"              
$playerLimit = 99999
$type = "ranked"                  
$Tiers = @("IRON","BRONZE","SILVER","GOLD","PLATINUM","EMERALD","DIAMOND")

foreach ($Tier in $Tiers) {
    Write-Host "🚀 Fetching matches for tier: $Tier"
    
    $Body = @{
        region = $region
        tier = $Tier
        playerLimit = $playerLimit
        type = $type
    } | ConvertTo-Json

    try {
        $Response = Invoke-RestMethod -Uri $URL -Method Post -Body $Body -ContentType "application/json"
        Write-Host "✅ Done for $Tier`n"
    }
    catch {
        Write-Host ("❌ Error for " + $Tier + ": " + $_.Exception.Message + "`n")
    }

    Start-Sleep -Seconds 2
}
