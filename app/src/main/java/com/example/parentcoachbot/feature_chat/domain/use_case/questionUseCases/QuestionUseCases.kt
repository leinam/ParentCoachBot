package com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases


data class QuestionUseCases(
    val getAllQuestions: GetAllQuestions,
    val deleteQuestion: DeleteQuestion,
    val addQuestion: AddQuestion,
    val getQuestionsWithAnswers: GetQuestionsWithAnswers,
    val getQuestionWithAnswers: GetQuestionWithAnswers,
    val getQuestionBySubtopic: GetQuestionsBySubtopic
) {

}