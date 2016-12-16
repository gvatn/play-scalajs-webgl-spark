package sparkConn

import akka.actor.ActorRef
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import sparkStream.{TwitterReceiver, WebSocketActor}

import scala.collection.mutable.ArrayBuffer

object SparkCommon {

  lazy val conf = {
    new SparkConf(false)
      .setMaster("local[*]")
      .setAppName("Testing")
      .set("spark.logConf", "true")
  }

  lazy val sc = SparkContext.getOrCreate(conf)
  // TODO: From sc
  lazy val ssc = new StreamingContext("local[*]", "StreamTest", Seconds(1))

  val allowedChars = (
    ('a' to 'z') ++
      ('0' to '9') ++
      Set('æ', 'ø', 'å')
    ).toSet
  val noiseWords = Set("rt")
  val mostUsedWords = Set(
    "og","i","det","på","som","er","en","til","å","han","av","for","med","at","var","de","ikke","den","har","jeg",
    "om","et","men","så","seg","hun","hadde","fra","vi","du","kan","da","ble","ut","skal","vil","ham","etter",
    "over","ved","også","bare","eller","sa","nå","dette","noe","være","meg","mot","opp","der","når","inn","dem",
    "kunne","andre","blir","alle","noen","sin","ha","år","henne","må","selv","sier","få","kom","denne","enn","to",
    "hans","bli","ville","før","vært","skulle","går","her","slik","gikk","mer","hva","igjen","fikk","man","alt",
    "mange","dash","ingen","får","oss","hvor","under","siden","hele","dag","gang","sammen","ned","kommer","sine",
    "deg","se","første","godt","mellom","måtte","gå","helt","litt","nok","store","aldri","ta","sig","uten","ho",
    "kanskje","blitt","ser","hvis","bergen","sitt","jo","vel","si","vet","hennes","min","tre","ja","samme","mye",
    "nye","tok","gjøre","disse","siste","tid","rundt","tilbake","mens","satt","flere","folk","","fordi","både",
    "la","gjennom","fått","like","nei","annet","komme","kroner","gjorde","hvordan","","norge","norske","gjør",
    "oslo","står","stor","gamle","langt","annen","sett","først","mener","hver","barn","rett","ny","tatt","derfor",
    "fram","hos","heller","lenge","alltid","tror","nesten","mann","gi","god","lå","blant","norsk","gjort","visste",
    "bak","tar","liv","mennesker","frem","bort","ein","verden","deres","ikkje","","tiden","del","vår","mest",
    "eneste","likevel","hatt","dei","tidligere","fire","liten","hvorfor","tenkte","hverandre","holdt","bedre",
    "meget","ting","lite","","stod","ei","hvert","begynte","gir","ligger","grunn","dere","livet","a","sagt",
    "land","","kommet","e","neste","far","etter","egen","side","gått","mor","ute","videre","","millioner",
    "prosent","svarte","sto","begge","allerede","inne","finne","enda","hjem","foran","måte","","mannen","dagen",
    "hodet","saken","ganger","kjente","stort","blev","mindre","","landet","byen","plass","kveld","ord","øynene",
    "fem","større","gode","nu","synes","beste","kvinner","ett","satte","hvem","all","","klart","holde","ofte",
    "stille","spurte","lenger","sted","dager","mulig","utenfor","små","frå","nytt","slike","viser","","mig",
    "kjenner","samtidig","senere","særlig","våre","akkurat","menn","hørte","mdash","arbeidet","altså","par","din",
    "unge","n","borte","plutselig","fant","fast","kunde","snart","svært","fall","vei","bergens","dessuten","forhold",
    "gjerne","snakket","foto","","snakke","bør","dersom","imidlertid","lett","tenke","gud","tro","","jan","gitt",
    "penger","egentlig","mitt","ønsker","ansiktet","kl","dermed","","slo","","politiet","faren","eit","bra","je",
    "sitter","sikkert","vite","full","lille","","glad","fleste","slutt","ene","mine","gjelder","lagt","virkelig",
    "laget","alene","ennå","lang","ganske","johan","omkring","hjemme","vårt","vanskelig","arne","gammel","skulde",
    "tidende","riktig","huset","følte","møte","lørdag","klar"
  )
}
