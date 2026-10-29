package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.MaqraahRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class MaqraahViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MaqraahRepository(application)

    private val _currentUserProfile = MutableStateFlow<UserProfile?>(null)
    val currentUserProfile: StateFlow<UserProfile?> = _currentUserProfile.asStateFlow()

    private val _currentLanguage = MutableStateFlow("am")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _fontSizeScale = MutableStateFlow(1f)
    val fontSizeScale: StateFlow<Float> = _fontSizeScale.asStateFlow()

    private val _chatColorOption = MutableStateFlow("Emerald")
    val chatColorOption: StateFlow<String> = _chatColorOption.asStateFlow()

    private val _announcements = MutableStateFlow<List<AnnouncementItem>>(emptyList())
    val announcements: StateFlow<List<AnnouncementItem>> = _announcements.asStateFlow()

    private val _studyGroups = MutableStateFlow<List<StudyGroup>>(emptyList())
    val studyGroups: StateFlow<List<StudyGroup>> = _studyGroups.asStateFlow()

    private val _stories = MutableStateFlow<List<StoryItem>>(emptyList())
    val stories: StateFlow<List<StoryItem>> = _stories.asStateFlow()

    private val _allUsers = MutableStateFlow<List<UserProfile>>(emptyList())
    val allUsers: StateFlow<List<UserProfile>> = _allUsers.asStateFlow()

    private val _friendshipRequests = MutableStateFlow<List<FriendshipRequest>>(emptyList())
    val friendshipRequests: StateFlow<List<FriendshipRequest>> = _friendshipRequests.asStateFlow()

    private val _programChangeRequests = MutableStateFlow<List<ProgramChangeRequest>>(emptyList())
    val programChangeRequests: StateFlow<List<ProgramChangeRequest>> = _programChangeRequests.asStateFlow()

    private val _classes = MutableStateFlow<List<LiveClassItem>>(emptyList())
    val classes: StateFlow<List<LiveClassItem>> = _classes.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _paymentRequests = MutableStateFlow<List<PaymentRequestItem>>(emptyList())
    val paymentRequests: StateFlow<List<PaymentRequestItem>> = _paymentRequests.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadInitialData()
    }

    fun setLanguage(lang: String) {
        _currentLanguage.value = lang
        viewModelScope.launch {
            val current = _currentUserProfile.value
            if (current != null) {
                val updated = current.copy(language = lang)
                _currentUserProfile.value = updated
                repository.saveUserProfile(updated)
            }
        }
    }

    fun setDarkMode(dark: Boolean) {
        _isDarkMode.value = dark
    }

    fun setFontSizeScale(scale: Float) {
        _fontSizeScale.value = scale
    }

    fun setChatColorOption(color: String) {
        _chatColorOption.value = color
    }

    fun loadInitialData() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            viewModelScope.launch {
                _isLoading.value = true
                val profile = repository.getUserProfile(user.uid)
                val isSuperAdmin = user.email == "khdahd241@gmail.com"
                if (profile != null) {
                    val updatedProfile = if (isSuperAdmin && profile.role != "super_admin") {
                        profile.copy(role = "super_admin")
                    } else {
                        profile
                    }
                    if (isSuperAdmin && profile.role != "super_admin") {
                        repository.saveUserProfile(updatedProfile)
                    }
                    _currentUserProfile.value = updatedProfile
                    _currentLanguage.value = updatedProfile.language.ifEmpty { "am" }
                } else {
                    val codePrefix = "IM"
                    val randNum = Random.nextInt(1000, 9999)
                    val newProfile = UserProfile(
                        id = user.uid,
                        name = if (isSuperAdmin) "አልጀዘሪ አድሚን" else (user.displayName ?: "ተማሪ"),
                        email = user.email ?: "",
                        role = if (isSuperAdmin) "super_admin" else "student",
                        studentCode = "$codePrefix-$randNum",
                        language = "am",
                        minuteBalance = if (isSuperAdmin) 9999 else 60
                    )
                    repository.saveUserProfile(newProfile)
                    _currentUserProfile.value = newProfile
                }
                _isLoading.value = false
            }
        }

        viewModelScope.launch {
            repository.observeAnnouncements().collect { _announcements.value = it }
        }
        viewModelScope.launch {
            repository.observeStudyGroups().collect { _studyGroups.value = it }
        }
        viewModelScope.launch {
            repository.observeStories().collect { _stories.value = it }
        }
        viewModelScope.launch {
            repository.observeAllUsers().collect { _allUsers.value = it }
        }
        viewModelScope.launch {
            val uid = Firebase.auth.currentUser?.uid ?: ""
            if (uid.isNotBlank()) {
                repository.observeFriendshipRequests(uid).collect { _friendshipRequests.value = it }
            }
        }
        viewModelScope.launch {
            repository.observeProgramChangeRequests().collect { _programChangeRequests.value = it }
        }
        viewModelScope.launch {
            repository.observeClasses().collect { _classes.value = it }
        }
        viewModelScope.launch {
            repository.observeMessages("general").collect { _messages.value = it }
        }
        viewModelScope.launch {
            repository.observePaymentRequests().collect { _paymentRequests.value = it }
        }
    }

    fun registerUser(
        name: String,
        age: Int,
        gender: String,
        city: String,
        phone: String,
        quranLevel: String,
        programType: String,
        shift: String,
        avatarEmoji: String
    ) {
        val user = Firebase.auth.currentUser ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val isFemale = gender.contains("ሴት") || gender.contains("Female")
            val codePrefix = if (isFemale) "IF" else "IM"
            val randNum = Random.nextInt(1000, 9999)
            val studentCode = "$codePrefix-$randNum"

            val profile = UserProfile(
                id = user.uid,
                name = name,
                email = user.email ?: "",
                role = if (user.email == "khdahd241@gmail.com") "super_admin" else "student",
                studentCode = studentCode,
                age = age,
                gender = gender,
                city = city,
                phone = phone,
                language = _currentLanguage.value,
                quranLevel = quranLevel,
                programType = programType,
                shift = shift,
                avatarEmoji = avatarEmoji,
                minuteBalance = 60
            )
            repository.saveUserProfile(profile)
            _currentUserProfile.value = profile
            _isLoading.value = false
        }
    }

    fun publishStory(title: String, content: String) {
        val profile = _currentUserProfile.value ?: return
        viewModelScope.launch {
            repository.publishStory(StoryItem(title = title, content = content, authorName = profile.name))
        }
    }

    fun sendFriendshipRequest(receiverId: String, receiverName: String, receiverCode: String) {
        val profile = _currentUserProfile.value ?: return
        viewModelScope.launch {
            repository.sendFriendshipRequest(
                FriendshipRequest(
                    senderId = profile.id,
                    senderName = profile.name,
                    senderCode = profile.studentCode,
                    receiverId = receiverId,
                    receiverName = receiverName,
                    status = "Pending"
                )
            )
        }
    }

    fun updateFriendshipStatus(requestId: String, status: String) {
        viewModelScope.launch {
            repository.updateFriendshipStatus(requestId, status)
        }
    }

    fun submitProgramChangeRequest(currentProg: String, newProg: String, reason: String) {
        val profile = _currentUserProfile.value ?: return
        viewModelScope.launch {
            repository.submitProgramChangeRequest(
                ProgramChangeRequest(
                    userId = profile.id,
                    userName = profile.name,
                    currentProgram = currentProg,
                    requestedProgram = newProg,
                    reason = reason,
                    status = "Pending"
                )
            )
        }
    }

    fun updateProgramChangeStatus(requestId: String, userId: String, newProgram: String, status: String) {
        viewModelScope.launch {
            repository.updateProgramChangeStatus(requestId, userId, newProgram, status)
        }
    }

    fun updateUserRole(userId: String, newRole: String) {
        viewModelScope.launch {
            repository.updateUserRole(userId, newRole)
        }
    }

    fun blockUser(userId: String, blocked: Boolean) {
        viewModelScope.launch {
            repository.blockUser(userId, blocked)
        }
    }

    fun addBonusMinutes(userId: String, minutes: Int) {
        viewModelScope.launch {
            repository.addBonusMinutes(userId, minutes)
        }
    }

    fun scheduleClass(title: String, teacherName: String, time: String) {
        viewModelScope.launch {
            repository.scheduleClass(LiveClassItem(title = title, teacherName = teacherName, scheduleTime = time, status = "Scheduled"))
        }
    }

    fun sendMessage(text: String, chatId: String = "general") {
        val user = Firebase.auth.currentUser
        val profile = _currentUserProfile.value
        if (user == null || text.isBlank()) return
        viewModelScope.launch {
            val msg = ChatMessage(
                chatId = chatId,
                senderId = user.uid,
                senderName = profile?.name ?: user.displayName ?: "ተጠቃሚ",
                text = text
            )
            repository.sendMessage(msg)
        }
    }

    fun submitPayment(amount: Int, minutes: Int, method: String) {
        val user = Firebase.auth.currentUser
        val profile = _currentUserProfile.value
        if (user == null) return
        viewModelScope.launch {
            val req = PaymentRequestItem(
                userId = user.uid,
                userName = profile?.name ?: user.displayName ?: "ተማሪ",
                amountEtb = amount,
                minutesRequested = minutes,
                paymentMethod = method,
                status = "Pending"
            )
            repository.submitPaymentRequest(req)
        }
    }

    fun approvePayment(requestId: String, userId: String, minutes: Int) {
        viewModelScope.launch {
            repository.updatePaymentStatus(requestId, "Approved", userId, minutes)
        }
    }

    fun postAnnouncement(title: String, body: String) {
        viewModelScope.launch {
            repository.postAnnouncement(AnnouncementItem(title = title, body = body, authorName = _currentUserProfile.value?.name ?: "Admin"))
        }
    }
}
