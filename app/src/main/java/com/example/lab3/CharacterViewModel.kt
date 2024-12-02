import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab3.CharacterEntity
import com.example.lab3.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CharacterViewModel(private val repository: CharacterRepository) : ViewModel() {

    // Состояние текущей страницы
    private var currentPage = 1

    // Flow для получения списка персонажей
    val characters: Flow<List<CharacterEntity>> = repository.getCharactersFromDatabase()

    // Загружаем персонажей для текущей страницы
    fun refreshCharacters(page: Int) {
        viewModelScope.launch {
            currentPage = page
            repository.refreshCharacters(page, 50)  // Загружаем данные с API для текущей страницы
        }
    }

    // Переход на следующую страницу
    fun nextPage() {
        refreshCharacters(currentPage + 1)
    }

    // Переход на предыдущую страницу
    fun previousPage() {
        if (currentPage > 1) {
            refreshCharacters(currentPage - 1)
        }
    }
}
