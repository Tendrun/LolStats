$URL = "http://localhost:4020/api/database/FetchMatchDetails"
$region = "europe"              
$playerMatchLimit = 99999

foreach ($Tier in $Tiers) {
    Write-Host "🚀 Fetching match details for tier: $Tier"
    
    $Body = @{
        region = $region
        playerMatchLimit = $playerMatchLimit
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
