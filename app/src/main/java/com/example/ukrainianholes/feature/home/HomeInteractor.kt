package com.example.ukrainianholes.feature.home

import com.example.ukrainianholes.architecture.base.BaseInteractor
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.architecture.base.ResultSuccess
import com.example.ukrainianholes.data.remote.ApiService

class HomeInteractor(
    private val apiService: ApiService
) : BaseInteractor() {
    suspend fun getLastWins(): Result<List<LastWin>> {
        return ResultSuccess(
            listOf(
                LastWin(
                    0,
                    "Відремонтовано",
                    "м. Київ, вул. Єлізавети Чавдар 13",
                    "13.06.2019",
                    "7 днів"
                ),
                LastWin(
                    1,
                    "Відремонтовано",
                    "м. Запоріжжя, вул. Бочарова 10",
                    "10.07.2019",
                    "21 день"
                ),
                LastWin(
                    2,
                    "Відремонтовано",
                    "м. Київ, вул. Григорія Чупринки 2/8",
                    "01.11.2019",
                    "22 дні"
                )
            )
        )
        /*return processRequest {
            apiService.registerUser(UserBody(email, password))
        }*/
    }
}