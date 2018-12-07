import org.nield.kotlinstatistics.NaiveBayesClassifier
import org.nield.kotlinstatistics.toNaiveBayesClassifier
import java.io.File
import java.lang.Math.pow
import kotlin.math.roundToInt

lateinit var nbc: NaiveBayesClassifier<String, String>
fun main(){
//coxBFPyK9XSvIEJFtneXpFv9jIZ37MfTMoQtzaweu2yrnXA18nAzOuMzsA8AEp8D

    val matches = arrayListOf<Match>()
    val f = File("matches.csv")

    f.forEachLine {
        val chunks = it.split(",")
        matches.add(Match("red${chunks[1]},red${chunks[2]},red${chunks[3]}", "blue${chunks[4]},blue${chunks[5]},blue${chunks[6]}", if(chunks[7] > chunks[8]) "red" else "blue"))
    }

    println("calculating bayesian boi")
    nbc = matches.toSet().toNaiveBayesClassifier(
        featuresSelector = { it.red.split(",").toSet().plus(it.blue.split(",")) },
        categorySelector = { it.winner }
    )

    nbc.predict()

    println("done")
    while(true){
        print("team: ")
        val t = readLine() ?: ""
        println(powerLevel(t) * 100)
    }

}

fun predictMatch(red: String, blue: String){

    val r = red.split(",","\t").map {
        "red$it"
    }
    val b = blue.split(",","\t").map {
        "blue$it"
    }

    val p = nbc.predictWithProbability(r.plus(b))
    if(p == null) {println("No prediction"); return}
    println("${(p.probability*100).roundToInt()}% ${p.category}")
}

fun powerLevel(team: String) : Double {
    val outcome = nbc.predictWithProbability("red$team")
    if(outcome != null){
        var pc = outcome.probability
        if(outcome.category == "blue") pc *= -1
        return pow((pc+1)/2,2.0)
    } else {
        return 0.0
    }

}

data class Match(val red: String, val blue: String, val winner: String)