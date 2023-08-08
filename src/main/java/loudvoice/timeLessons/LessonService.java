package loudvoice.timeLessons;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class LessonService implements ILessonService{
    private final LessonRepository lessonRepository;

    @Override
    public List<timeLessons> getAllTime() {
        List<timeLessons> time = lessonRepository.findAll();
        for (timeLessons a : time){
            Calendar calendar = Calendar.getInstance();
            if ((a.getExpirationTime().getTime()-calendar.getTime().getTime()) <= 0){
                lessonRepository.deleteById(a.getId());
            }
        }
        time.sort(Comparator.comparing(timeLessons::getDateIn));
        return time;
    }

    @Override
    public Optional<timeLessons> findById(Long id) {
        return lessonRepository.findById(id);
    }
    @Transactional
    @Override
    public List<timeLessons> getRecordedTime() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<timeLessons> time = lessonRepository.findAll();
        List<timeLessons> timeOut = new ArrayList<>();
        for (timeLessons t :time) {
            if (t.getUid()!=null){
                timeOut.add(t);
            }
        }
        timeOut.sort(Comparator.comparing(timeLessons::getDateIn));
        return timeOut;
    }

    @Transactional
    @Override
    public List<timeLessons> getAllMyTime(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<timeLessons> time = lessonRepository.findAll();
        List<timeLessons> timeOut = new ArrayList<>();
        for (timeLessons t : time){
            if (t.getUid() == null){
                timeOut.add(t);
            }
        }
        timeOut.sort(Comparator.comparing(timeLessons::getDateIn));
        return timeOut;
    }

    @Transactional
    @Override
    public List<timeLessons> getLessTime(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<timeLessons> time = lessonRepository.findAll();
        List<timeLessons> timeOut = new ArrayList<>();
        for (timeLessons t :time) {
            if (t.getUid()!=null && t.getUid().equals(currentPrincipalName)){
                timeOut.add(t);
            }
        }
        timeOut.sort(Comparator.comparing(timeLessons::getDateIn));
        return timeOut;
    }


    @Transactional
    @Override
    public void addDateId(String Uid, Long id){
        Optional<timeLessons> addDateId = lessonRepository.findById(id);
        addDateId.ifPresent(time -> lessonRepository.addDateId(true,Uid,id));
    }

    @Transactional
    @Override
    public void DeleteDateId(String Uid,Long id){
        Optional<timeLessons> DeleteDateId = lessonRepository.findById(id);
        DeleteDateId.ifPresent(time -> lessonRepository.DeleteDateId(false,null,id));
    }

    @Transactional
    @Override
    public void deleteLessonsTime(Long id){
        Optional<timeLessons> deleteLessonsTime = lessonRepository.findById(id);
        lessonRepository.DeleteDateId(false,null,id);
    }

    @Transactional
    @Override
    public void deleteTime(Long id) {
        Optional<timeLessons> theTime = lessonRepository.findById(id);
        theTime.ifPresent(user -> lessonRepository.deleteById(id));
    }
}
