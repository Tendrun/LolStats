$URL = "http://localhost:4020/api/database/FetchPlayers"
$Region = "eun1"              # UPPERCASE dla valueOf(...)
$Division = "II"
$Queue = "RANKED_SOLO_5x5"
$Page = 1                     # liczba, nie string
$Tiers = @("IRON","BRONZE","SILVER","GOLD","PLATINUM","EMERALD","DIAMOND")

foreach ($Tier in $Tiers) {
    Write-Host "🚀 Fetching players for tier: $Tier"
    
    $Body = @{
        region = $Region
        tier = $Tier
        division = $Division
        queue = $Queue
        page = $Page
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
