package main.kotlin.fr.iutna.lpmiar.td1.util

data class Event(
        var startTime: String,
        var endTime: String,
        var uid: String,
        var summary: String,
        var location: String,
        var description: String,
        var categories: String,
        var matiere: String?,
        var personnel: String?,
        var groupe: String?,
        var salle: String?
){
    override fun toString(): String {
        return "Event(startTime='$startTime', endTime='$endTime', uid='$uid', summary='$summary', location='$location', description='$description', categories='$categories', matiere=$matiere, personnel=$personnel, groupe=$groupe, salle=$salle)"
    }
}
