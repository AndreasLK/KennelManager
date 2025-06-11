
package com.diarreatracker

import android.content.Context
import com.example.diarreatracker.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * 1) Define all data-classes for Auth endpoints (login, validate_token, get permissions).
 * 2) Define all data-classes for Info endpoints (CRUD + search for each model).
 * 3) Retrofit interfaces: AuthService + InfoService.
 * 4) A singleton object ApiClient that:
 *    - Exposes `suspend fun login(...)`, `fun logout()`, `suspend fun getDogs(...)`, etc.
 *    - Manages Retrofit instances and a Bearer‐token interceptor.
 *    - Stores the JWT in memory (you can persist it to DataStore/EncryptedSharedPrefs if you like).
 */

/** ────────────────────────────────────────────────
 *  1) AUTH SERVER MODELS & SERVICE
 *  ────────────────────────────────────────────────
 */
@Serializable
data class UserCreate(
    val username: String,
    val password: String
)

@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String
)

@Serializable
data class TokenData(
    @SerialName("user_id") val userId: Int,
    @SerialName("permission_level") val permissionLevel: Int
)

@Serializable
data class PermissionsResponse(
    val permissions: List<String>
)

interface AuthService {
    @POST("/login")
    suspend fun login(@Body payload: UserCreate): TokenResponse

    @GET("/validate_token")
    suspend fun validateToken(@Header("Authorization") bearer: String): TokenData

    @GET("/user/permissions")
    suspend fun getPermissions(@Header("Authorization") bearer: String): PermissionsResponse

    @POST("/register")
    suspend fun register(@Body payload: UserCreate): UserReadResponse
}

@Serializable
data class UserReadResponse(
    @SerialName("UserID") val userId: Int,
    @SerialName("HashedUsername") val username: String,
    @SerialName("PermissionLevel") val permissionLevel: Int
)

/** ────────────────────────────────────────────────
 *  2) INFO SERVER MODELS & SERVICE
 *
 *  We define **all** the request/response models (matching your FastAPI `schemas.py`).
 *  Each “Read” response includes an `id` and `Author` fields; each “Create” omits `id` & `Author`.
 *  Dates are represented as Ints (Unix timestamps or YYYYMMDD) because your Python code used `int`.
 *  ────────────────────────────────────────────────
 */

/** ───────────────── Dogs ───────────────── */
@Serializable
data class RunCountResponse(
    @SerialName("dog_id") val dogId: Int,
    @SerialName("dog_name") val dogName: Int,
    @SerialName("days") val days: Int,
    @SerialName("run_count") val runCount: Int,
    @SerialName("total_distance") val totalDistance: Int
)

@Serializable
data class DogRead(
    @SerialName("DogID") val dogId: Int,
    @SerialName("DogName") val name: String,
    @SerialName("ParentGroupID") val parentGroupId: Int? = null,
    @SerialName("ChildGroupID") val childGroupId: Int? = null,
    @SerialName("Birthday") val birthday: Int,
    @SerialName("Author") val author: String
)

@Serializable
data class DogCreate(
    @SerialName("DogName") val name: String,
    @SerialName("ParentGroupID") val parentGroupId: Int? = null,
    @SerialName("ChildGroupID") val childGroupId: Int? = null,
    @SerialName("Birthday") val birthday: Int
)

/** ───────────────── Cages ───────────────── */
@Serializable
data class CageRead(
    @SerialName("CageID") val cageId: Int,
    @SerialName("CageRow") val cageRow: String,
    @SerialName("Longtitude") val longitude: Double,
    @SerialName("Lattitude") val latitude: Double,
    @SerialName("Width") val width: Int,
    @SerialName("Height") val height: Int,
    @SerialName("Author") val author: String
)

@Serializable
data class CageCreate(
    @SerialName("CageRow") val cageRow: String,
    @SerialName("Longtitude") val longitude: Double,
    @SerialName("Lattitude") val latitude: Double,
    @SerialName("Width") val width: Int,
    @SerialName("Height") val height: Int
)

/** ───────────────── BirthGroups ───────────────── */
@Serializable
data class BirthGroupRead(
    @SerialName("BirthGroupID") val birthGroupId: Int,
    @SerialName("Author") val author: String,
    @SerialName("Date") val date: Int
)

@Serializable
data class BirthGroupCreate(
    @SerialName("Date") val date: Int
)

/** ───────────────── Heats ───────────────── */
@Serializable
data class HeatRead(
    @SerialName("HeatID") val heatId: Int,
    @SerialName("Date") val date: Int,
    @SerialName("DogID") val dogId: Int,
    @SerialName("Author") val author: String
)

@Serializable
data class HeatCreate(
    @SerialName("Date") val date: Int,
    @SerialName("DogID") val dogId: Int
)

/** ───────────────── PoopScores ───────────────── */
@Serializable
data class PoopscoreRead(
    @SerialName("PoopscoreID") val poopscoreId: Int,
    @SerialName("DogID") val dogId: Int,
    @SerialName("Date") val date: Int,
    @SerialName("Score") val score: Int,
    @SerialName("Author") val author: String
)

@Serializable
data class PoopscoreCreate(
    @SerialName("DogID") val dogId: Int,
    @SerialName("Date") val date: Int,
    @SerialName("Score") val score: Int
)

/** ───────────────── Runs ───────────────── */
@Serializable
data class RunRead(
    @SerialName("RunID") val runId: Int,
    @SerialName("DogID") val dogId: Int,
    @SerialName("Distance") val distance: Int,
    @SerialName("Type") val type: String,
    @SerialName("Author") val author: String
)

@Serializable
data class RunCreate(
    @SerialName("DogID") val dogId: Int,
    @SerialName("Distance") val distance: Int,
    @SerialName("Type") val type: String
)

/** ───────────────── Vaccines ───────────────── */
@Serializable
data class VaccineRead(
    @SerialName("VaccineID") val vaccineId: Int,
    @SerialName("DogID") val dogId: Int,
    @SerialName("Type") val type: String,
    @SerialName("DateOfVaccine") val dateOfVaccine: Int,
    @SerialName("DateOfEffectiveness") val dateOfEffectiveness: Int,
    @SerialName("DateOfExpiry") val dateOfExpiry: Int,
    @SerialName("Author") val author: String
)

@Serializable
data class VaccineCreate(
    @SerialName("DogID") val dogId: Int,
    @SerialName("Type") val type: String,
    @SerialName("DateOfVaccine") val dateOfVaccine: Int,
    @SerialName("DateOfEffectiveness") val dateOfEffectiveness: Int,
    @SerialName("DateOfExpiry") val dateOfExpiry: Int
)

/** ───────────────── VetVisits ───────────────── */
@Serializable
data class VetVisitRead(
    @SerialName("VetVisitID") val vetVisitId: Int,
    @SerialName("DogID") val dogId: Int,
    @SerialName("Vet") val vet: String,
    @SerialName("Date") val date: Int,
    @SerialName("Notes") val notes: String,
    @SerialName("Author") val author: String
)

@Serializable
data class VetVisitCreate(
    @SerialName("DogID") val dogId: Int,
    @SerialName("Vet") val vet: String,
    @SerialName("Date") val date: Int,
    @SerialName("Notes") val notes: String
)

/** ────────────────────────────────────────────────
 *  3) INFO SERVICE INTERFACE (all endpoints)
 *  ────────────────────────────────────────────────
 */
interface InfoService {

    // ─── 1) BirthGroup search ────────────────────────────────────────────────
    @GET("/birthgroups/search")
    suspend fun searchBirthGroups(
        @Query("author") author: String? = null,
        @Query("date_min") dateMin: Int? = null,
        @Query("date_max") dateMax: Int? = null
    ): List<BirthGroupRead>

    // CRUD for BirthGroup
    @POST("/birthgroups/")
    suspend fun createBirthGroup(@Body payload: BirthGroupCreate): BirthGroupRead

    @GET("/birthgroups/")
    suspend fun listBirthGroups(): List<BirthGroupRead>

    @GET("/birthgroups/{id}")
    suspend fun getBirthGroup(@Path("id") id: Int): BirthGroupRead

    @PUT("/birthgroups/{id}")
    suspend fun updateBirthGroup(
        @Path("id") id: Int,
        @Body payload: BirthGroupCreate
    ): BirthGroupRead

    @DELETE("/birthgroups/{id}")
    suspend fun deleteBirthGroup(@Path("id") id: Int)

    // ─── 2) Cage search ─────────────────────────────────────────────────────
    @GET("/cages/search")
    suspend fun searchCages(
        @Query("cagerow_exact") cageRowExact: String? = null,
        @Query("cagerow_like") cageRowLike: String? = null,
        @Query("min_longitude") minLongitude: Double? = null,
        @Query("max_longitude") maxLongitude: Double? = null,
        @Query("min_latitude") minLatitude: Double? = null,
        @Query("max_latitude") maxLatitude: Double? = null,
        @Query("width") width: Int? = null,
        @Query("height") height: Int? = null,
        @Query("author") author: String? = null
    ): List<CageRead>

    // CRUD for Cage
    @POST("/cages/")
    suspend fun createCage(@Body payload: CageCreate): CageRead

    @GET("/cages/")
    suspend fun listCages(): List<CageRead>

    @GET("/cages/{id}")
    suspend fun getCage(@Path("id") id: Int): CageRead

    @PUT("/cages/{id}")
    suspend fun updateCage(
        @Path("id") id: Int,
        @Body payload: CageCreate
    ): CageRead

    @DELETE("/cages/{id}")
    suspend fun deleteCage(@Path("id") id: Int)

    // ─── 3) Dog search ──────────────────────────────────────────────────────
    @GET("/dogs/search")
    suspend fun searchDogs(
        @Query("name_like") nameLike: String? = null,
        @Query("parent_group_id") parentGroupId: Int? = null,
        @Query("child_group_id") childGroupId: Int? = null,
        @Query("birthday_min") birthdayMin: Int? = null,
        @Query("birthday_max") birthdayMax: Int? = null,
        @Query("author") author: String? = null
    ): List<DogRead>

    @GET("/dogs/{id}/run_count")
    suspend fun getRunCount(
        @Path("id") dogId: Int,
        @Query("days") days: Int = 7
    ): RunCountResponse

    // CRUD for Dog
    @POST("/dogs/")
    suspend fun createDog(@Body payload: DogCreate): DogRead

    @GET("/dogs/")
    suspend fun listDogs(): List<DogRead>

    @GET("/dogs/{id}")
    suspend fun getDog(@Path("id") id: Int): DogRead

    @PUT("/dogs/{id}")
    suspend fun updateDog(
        @Path("id") id: Int,
        @Body payload: DogCreate
    ): DogRead

    @DELETE("/dogs/{id}")
    suspend fun deleteDog(@Path("id") id: Int)

    // ─── 4) Heat search ─────────────────────────────────────────────────────
    @GET("/heats/search")
    suspend fun searchHeats(
        @Query("date_min") dateMin: Int? = null,
        @Query("date_max") dateMax: Int? = null,
        @Query("dog_id") dogId: Int? = null,
        @Query("author") author: String? = null
    ): List<HeatRead>

    // CRUD for Heat
    @POST("/heats/")
    suspend fun createHeat(@Body payload: HeatCreate): HeatRead

    @GET("/heats/")
    suspend fun listHeats(): List<HeatRead>

    @GET("/heats/{id}")
    suspend fun getHeat(@Path("id") id: Int): HeatRead

    @PUT("/heats/{id}")
    suspend fun updateHeat(
        @Path("id") id: Int,
        @Body payload: HeatCreate
    ): HeatRead

    @DELETE("/heats/{id}")
    suspend fun deleteHeat(@Path("id") id: Int)

    // ─── 5) Poopscore search ─────────────────────────────────────────────────
    @GET("/poopscores/search")
    suspend fun searchPoopScores(
        @Query("dog_id") dogId: Int? = null,
        @Query("date_min") dateMin: Int? = null,
        @Query("date_max") dateMax: Int? = null,
        @Query("score_min") scoreMin: Int? = null,
        @Query("score_max") scoreMax: Int? = null,
        @Query("author") author: String? = null
    ): List<PoopscoreRead>

    // CRUD for Poopscore
    @POST("/poopscores/")
    suspend fun createPoopscore(@Body payload: PoopscoreCreate): PoopscoreRead

    @GET("/poopscores/")
    suspend fun listPoopScores(): List<PoopscoreRead>

    @GET("/poopscores/{id}")
    suspend fun getPoopscore(@Path("id") id: Int): PoopscoreRead

    @PUT("/poopscores/{id}")
    suspend fun updatePoopscore(
        @Path("id") id: Int,
        @Body payload: PoopscoreCreate
    ): PoopscoreRead

    @DELETE("/poopscores/{id}")
    suspend fun deletePoopscore(@Path("id") id: Int)

    // ─── 6) Run search ─────────────────────────────────────────────────────
    @GET("/runs/search")
    suspend fun searchRuns(
        @Query("dog_id") dogId: Int? = null,
        @Query("distance_min") distanceMin: Int? = null,
        @Query("distance_max") distanceMax: Int? = null,
        @Query("type_like") typeLike: String? = null,
        @Query("author") author: String? = null
    ): List<RunRead>

    // CRUD for Run
    @POST("/runs/")
    suspend fun createRun(@Body payload: RunCreate): RunRead

    @GET("/runs/")
    suspend fun listRuns(): List<RunRead>

    @GET("/runs/{id}")
    suspend fun getRun(@Path("id") id: Int): RunRead

    @PUT("/runs/{id}")
    suspend fun updateRun(
        @Path("id") id: Int,
        @Body payload: RunCreate
    ): RunRead

    @DELETE("/runs/{id}")
    suspend fun deleteRun(@Path("id") id: Int)

    // ─── 7) Vaccine search ──────────────────────────────────────────────────
    @GET("/vaccines/search")
    suspend fun searchVaccines(
        @Query("dog_id") dogId: Int? = null,
        @Query("type_exact") typeExact: String? = null,
        @Query("type_like") typeLike: String? = null,
        @Query("dov_min") dovMin: Int? = null,
        @Query("dov_max") dovMax: Int? = null,
        @Query("doe_min") doeMin: Int? = null,
        @Query("doe_max") doeMax: Int? = null,
        @Query("doexp_min") doexpMin: Int? = null,
        @Query("doexp_max") doexpMax: Int? = null,
        @Query("author") author: String? = null
    ): List<VaccineRead>

    // CRUD for Vaccine
    @POST("/vaccines/")
    suspend fun createVaccine(@Body payload: VaccineCreate): VaccineRead

    @GET("/vaccines/")
    suspend fun listVaccines(): List<VaccineRead>

    @GET("/vaccines/{id}")
    suspend fun getVaccine(@Path("id") id: Int): VaccineRead

    @PUT("/vaccines/{id}")
    suspend fun updateVaccine(
        @Path("id") id: Int,
        @Body payload: VaccineCreate
    ): VaccineRead

    @DELETE("/vaccines/{id}")
    suspend fun deleteVaccine(@Path("id") id: Int)

    // ─── 8) VetVisit search ─────────────────────────────────────────────────
    @GET("/vetvisits/search")
    suspend fun searchVetVisits(
        @Query("dog_id") dogId: Int? = null,
        @Query("vet_exact") vetExact: String? = null,
        @Query("vet_like") vetLike: String? = null,
        @Query("date_min") dateMin: Int? = null,
        @Query("date_max") dateMax: Int? = null,
        @Query("notes_like") notesLike: String? = null,
        @Query("author") author: String? = null
    ): List<VetVisitRead>

    // CRUD for VetVisit
    @POST("/vetvisits/")
    suspend fun createVetVisit(@Body payload: VetVisitCreate): VetVisitRead

    @GET("/vetvisits/")
    suspend fun listVetVisits(): List<VetVisitRead>

    @GET("/vetvisits/{id}")
    suspend fun getVetVisit(@Path("id") id: Int): VetVisitRead

    @PUT("/vetvisits/{id}")
    suspend fun updateVetVisit(
        @Path("id") id: Int,
        @Body payload: VetVisitCreate
    ): VetVisitRead

    @DELETE("/vetvisits/{id}")
    suspend fun deleteVetVisit(@Path("id") id: Int)
}

/** ────────────────────────────────────────────────
 *  4) API CLIENT SINGLETON
 *
 *  - Stores the JWT in memory (you can replace this with DataStore if you like).
 *  - Automatically injects “Authorization: Bearer <token>” on every call to InfoService.
 *  - Exposes coroutine‐safe functions to:
 *     • login(username, password)
 *     • logout()
 *     • getDogs(), createDog(...), updateDog(...), deleteDog(...)
 *     • same for cages, heats, poopscores, runs, vaccines, vetvisits, birthgroups.
 *  - Uses Kotlinx-serialization with Retrofit’s converter.
 *  ────────────────────────────────────────────────
 */
object ApiClient {
    // ─── CONFIGURATION: Point these to your FastAPI servers ───────────────────
    private const val AUTH_BASE_URL = "https://192.168.88.56:4000"
    private const val INFO_BASE_URL = "https://0.0.0.0:8001"

    @Volatile
    private var jwtToken: String? = null

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    private val contentTypeJson = "application/json".toMediaType()

    private lateinit var authService: AuthService
    private lateinit var infoService: InfoService


    /** Interceptor to add JWT token if available. */
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()
        jwtToken?.let { token ->
            builder.addHeader("Authorization", "Bearer $token")
        }
        chain.proceed(builder.build())
    }

    /** OkHttpClient builder that adds your cert and optional authInterceptor. */
    private fun getCustomOkHttpClient(context: Context, withAuthInterceptor: Boolean = false): OkHttpClient {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val inputStream = context.resources.openRawResource(R.raw.certificate) // YOUR CERT HERE
        val certificate = certificateFactory.generateCertificate(inputStream)
        inputStream.close()

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", certificate)

        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)
        val trustManager = trustManagerFactory.trustManagers[0] as X509TrustManager

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), null)

        val builder = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)

        if (withAuthInterceptor) {
            builder.addInterceptor(authInterceptor)
        }

        return builder.build()
    }

    /** Call this early like in Application class or Splash Screen */
    fun initialize(context: Context) {
        if (!::authService.isInitialized) {
            val retrofitAuth = Retrofit.Builder()
                .baseUrl(AUTH_BASE_URL)
                .addConverterFactory(json.asConverterFactory(contentTypeJson))
                .client(getCustomOkHttpClient(context)) // Without authInterceptor
                .build()

            authService = retrofitAuth.create(AuthService::class.java)
        }

        if (!::infoService.isInitialized) {
            val retrofitInfo = Retrofit.Builder()
                .baseUrl(INFO_BASE_URL)
                .addConverterFactory(json.asConverterFactory(contentTypeJson))
                .client(getCustomOkHttpClient(context, withAuthInterceptor = true)) // With authInterceptor
                .build()

            infoService = retrofitInfo.create(InfoService::class.java)
        }
    }


    suspend fun login(username: String, password: String) = withContext(Dispatchers.IO) {
        val payload = UserCreate(username = username, password = password)
        val response = authService.login(payload)
        jwtToken = response.accessToken
        response
    }

    fun logout() {
        jwtToken = null
    }

    suspend fun validateToken(): TokenData = withContext(Dispatchers.IO) {
        val token = jwtToken ?: throw IllegalStateException("Not logged in")
        authService.validateToken("Bearer $token")
    }

    suspend fun getPermissions(): PermissionsResponse = withContext(Dispatchers.IO) {
        val token = jwtToken ?: throw IllegalStateException("Not logged in")
        authService.getPermissions("Bearer $token")
    }

    // ─── DOG ENDPOINTS ──────────────────────────────────────────────────────

    suspend fun searchDogs(
        nameLike: String? = null,
        parentGroupId: Int? = null,
        childGroupId: Int? = null,
        birthdayMin: Int? = null,
        birthdayMax: Int? = null,
        author: String? = null
    ): List<DogRead> = withContext(Dispatchers.IO) {
        infoService.searchDogs(
            nameLike = nameLike,
            parentGroupId = parentGroupId,
            childGroupId = childGroupId,
            birthdayMin = birthdayMin,
            birthdayMax = birthdayMax,
            author = author
        )
    }

    suspend fun getRunCount(dogId: Int, days: Int = 7): RunCountResponse = withContext(Dispatchers.IO) {
        infoService.getRunCount(dogId, days)
    }

    suspend fun listDogs(): List<DogRead> = withContext(Dispatchers.IO) {
        infoService.listDogs()
    }

    suspend fun getDog(id: Int): DogRead = withContext(Dispatchers.IO) {
        infoService.getDog(id)
    }

    suspend fun createDog(payload: DogCreate): DogRead = withContext(Dispatchers.IO) {
        infoService.createDog(payload)
    }

    suspend fun updateDog(id: Int, payload: DogCreate): DogRead = withContext(Dispatchers.IO) {
        infoService.updateDog(id, payload)
    }

    suspend fun deleteDog(id: Int) = withContext(Dispatchers.IO) {
        infoService.deleteDog(id)
    }

    // ─── CAGE ENDPOINTS ─────────────────────────────────────────────────────

    suspend fun searchCages(
        cageRowExact: String? = null,
        cageRowLike: String? = null,
        minLongitude: Double? = null,
        maxLongitude: Double? = null,
        minLatitude: Double? = null,
        maxLatitude: Double? = null,
        width: Int? = null,
        height: Int? = null,
        author: String? = null
    ): List<CageRead> = withContext(Dispatchers.IO) {
        infoService.searchCages(
            cageRowExact = cageRowExact,
            cageRowLike = cageRowLike,
            minLongitude = minLongitude,
            maxLongitude = maxLongitude,
            minLatitude = minLatitude,
            maxLatitude = maxLatitude,
            width = width,
            height = height,
            author = author
        )
    }

    suspend fun listCages(): List<CageRead> = withContext(Dispatchers.IO) {
        infoService.listCages()
    }

    suspend fun getCage(id: Int): CageRead = withContext(Dispatchers.IO) {
        infoService.getCage(id)
    }

    suspend fun createCage(payload: CageCreate): CageRead = withContext(Dispatchers.IO) {
        infoService.createCage(payload)
    }

    suspend fun updateCage(id: Int, payload: CageCreate): CageRead = withContext(Dispatchers.IO) {
        infoService.updateCage(id, payload)
    }

    suspend fun deleteCage(id: Int) = withContext(Dispatchers.IO) {
        infoService.deleteCage(id)
    }

    // ─── BIRTHGROUP ENDPOINTS ───────────────────────────────────────────────

    suspend fun searchBirthGroups(
        author: String? = null,
        dateMin: Int? = null,
        dateMax: Int? = null
    ): List<BirthGroupRead> = withContext(Dispatchers.IO) {
        infoService.searchBirthGroups(author = author, dateMin = dateMin, dateMax = dateMax)
    }

    suspend fun listBirthGroups(): List<BirthGroupRead> = withContext(Dispatchers.IO) {
        infoService.listBirthGroups()
    }

    suspend fun getBirthGroup(id: Int): BirthGroupRead = withContext(Dispatchers.IO) {
        infoService.getBirthGroup(id)
    }

    suspend fun createBirthGroup(payload: BirthGroupCreate): BirthGroupRead = withContext(Dispatchers.IO) {
        infoService.createBirthGroup(payload)
    }

    suspend fun updateBirthGroup(id: Int, payload: BirthGroupCreate): BirthGroupRead = withContext(Dispatchers.IO) {
        infoService.updateBirthGroup(id, payload)
    }

    suspend fun deleteBirthGroup(id: Int) = withContext(Dispatchers.IO) {
        infoService.deleteBirthGroup(id)
    }

    // ─── HEAT ENDPOINTS ─────────────────────────────────────────────────────

    suspend fun searchHeats(
        dateMin: Int? = null,
        dateMax: Int? = null,
        dogId: Int? = null,
        author: String? = null
    ): List<HeatRead> = withContext(Dispatchers.IO) {
        infoService.searchHeats(dateMin = dateMin, dateMax = dateMax, dogId = dogId, author = author)
    }

    suspend fun listHeats(): List<HeatRead> = withContext(Dispatchers.IO) {
        infoService.listHeats()
    }

    suspend fun getHeat(id: Int): HeatRead = withContext(Dispatchers.IO) {
        infoService.getHeat(id)
    }

    suspend fun createHeat(payload: HeatCreate): HeatRead = withContext(Dispatchers.IO) {
        infoService.createHeat(payload)
    }

    suspend fun updateHeat(id: Int, payload: HeatCreate): HeatRead = withContext(Dispatchers.IO) {
        infoService.updateHeat(id, payload)
    }

    suspend fun deleteHeat(id: Int) = withContext(Dispatchers.IO) {
        infoService.deleteHeat(id)
    }

    // ─── POOPSCORE ENDPOINTS ─────────────────────────────────────────────────

    suspend fun searchPoopScores(
        dogId: Int? = null,
        dateMin: Int? = null,
        dateMax: Int? = null,
        scoreMin: Int? = null,
        scoreMax: Int? = null,
        author: String? = null
    ): List<PoopscoreRead> = withContext(Dispatchers.IO) {
        infoService.searchPoopScores(
            dogId = dogId,
            dateMin = dateMin,
            dateMax = dateMax,
            scoreMin = scoreMin,
            scoreMax = scoreMax,
            author = author
        )
    }

    suspend fun listPoopScores(): List<PoopscoreRead> = withContext(Dispatchers.IO) {
        infoService.listPoopScores()
    }

    suspend fun getPoopscore(id: Int): PoopscoreRead = withContext(Dispatchers.IO) {
        infoService.getPoopscore(id)
    }

    suspend fun createPoopscore(payload: PoopscoreCreate): PoopscoreRead = withContext(Dispatchers.IO) {
        infoService.createPoopscore(payload)
    }

    suspend fun updatePoopscore(id: Int, payload: PoopscoreCreate): PoopscoreRead = withContext(Dispatchers.IO) {
        infoService.updatePoopscore(id, payload)
    }

    suspend fun deletePoopscore(id: Int) = withContext(Dispatchers.IO) {
        infoService.deletePoopscore(id)
    }

    // ─── RUN ENDPOINTS ───────────────────────────────────────────────────────

    suspend fun searchRuns(
        dogId: Int? = null,
        distanceMin: Int? = null,
        distanceMax: Int? = null,
        typeLike: String? = null,
        author: String? = null
    ): List<RunRead> = withContext(Dispatchers.IO) {
        infoService.searchRuns(
            dogId = dogId,
            distanceMin = distanceMin,
            distanceMax = distanceMax,
            typeLike = typeLike,
            author = author
        )
    }

    suspend fun listRuns(): List<RunRead> = withContext(Dispatchers.IO) {
        infoService.listRuns()
    }

    suspend fun getRun(id: Int): RunRead = withContext(Dispatchers.IO) {
        infoService.getRun(id)
    }

    suspend fun createRun(payload: RunCreate): RunRead = withContext(Dispatchers.IO) {
        infoService.createRun(payload)
    }

    suspend fun updateRun(id: Int, payload: RunCreate): RunRead = withContext(Dispatchers.IO) {
        infoService.updateRun(id, payload)
    }

    suspend fun deleteRun(id: Int) = withContext(Dispatchers.IO) {
        infoService.deleteRun(id)
    }

    // ─── VACCINE ENDPOINTS ──────────────────────────────────────────────────

    suspend fun searchVaccines(
        dogId: Int? = null,
        typeExact: String? = null,
        typeLike: String? = null,
        dovMin: Int? = null,
        dovMax: Int? = null,
        doeMin: Int? = null,
        doeMax: Int? = null,
        doexpMin: Int? = null,
        doexpMax: Int? = null,
        author: String? = null
    ): List<VaccineRead> = withContext(Dispatchers.IO) {
        infoService.searchVaccines(
            dogId = dogId,
            typeExact = typeExact,
            typeLike = typeLike,
            dovMin = dovMin,
            dovMax = dovMax,
            doeMin = doeMin,
            doeMax = doeMax,
            doexpMin = doexpMin,
            doexpMax = doexpMax,
            author = author
        )
    }

    suspend fun listVaccines(): List<VaccineRead> = withContext(Dispatchers.IO) {
        infoService.listVaccines()
    }

    suspend fun getVaccine(id: Int): VaccineRead = withContext(Dispatchers.IO) {
        infoService.getVaccine(id)
    }

    suspend fun createVaccine(payload: VaccineCreate): VaccineRead = withContext(Dispatchers.IO) {
        infoService.createVaccine(payload)
    }

    suspend fun updateVaccine(id: Int, payload: VaccineCreate): VaccineRead = withContext(Dispatchers.IO) {
        infoService.updateVaccine(id, payload)
    }

    suspend fun deleteVaccine(id: Int) = withContext(Dispatchers.IO) {
        infoService.deleteVaccine(id)
    }

    // ─── VETVISIT ENDPOINTS ──────────────────────────────────────────────────

    suspend fun searchVetVisits(
        dogId: Int? = null,
        vetExact: String? = null,
        vetLike: String? = null,
        dateMin: Int? = null,
        dateMax: Int? = null,
        notesLike: String? = null,
        author: String? = null
    ): List<VetVisitRead> = withContext(Dispatchers.IO) {
        infoService.searchVetVisits(
            dogId = dogId,
            vetExact = vetExact,
            vetLike = vetLike,
            dateMin = dateMin,
            dateMax = dateMax,
            notesLike = notesLike,
            author = author
        )
    }

    suspend fun listVetVisits(): List<VetVisitRead> = withContext(Dispatchers.IO) {
        infoService.listVetVisits()
    }

    suspend fun getVetVisit(id: Int): VetVisitRead = withContext(Dispatchers.IO) {
        infoService.getVetVisit(id)
    }

    suspend fun createVetVisit(payload: VetVisitCreate): VetVisitRead = withContext(Dispatchers.IO) {
        infoService.createVetVisit(payload)
    }

    suspend fun updateVetVisit(id: Int, payload: VetVisitCreate): VetVisitRead = withContext(Dispatchers.IO) {
        infoService.updateVetVisit(id, payload)
    }

    suspend fun deleteVetVisit(id: Int) = withContext(Dispatchers.IO) {
        infoService.deleteVetVisit(id)
    }
}
