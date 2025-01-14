package fit.fitspring.service;

import fit.fitspring.controller.dto.communal.*;
import fit.fitspring.controller.dto.trainer.TrainerInformationDto;
import fit.fitspring.domain.account.*;
import fit.fitspring.domain.review.Review;
import fit.fitspring.domain.trainer.*;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static fit.fitspring.exception.common.ErrorCode.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunalService {

    private final AnnouncementRepository announcementRepository;
    private final TermRepository termRepository;
    private final TrainerRepository trainerRepository;
    private final UserImgRepository userImgRepository;
    private final AccountRepository accountRepository;
    private final TermAgreeRepository termAgreeRepository;

    @Transactional
    public List<AnnouncementDto> getAnnouncementList() {
        List<Announcement> annoList = announcementRepository.findAll();
        List<AnnouncementDto> annoDtoList = new ArrayList<>();
        for(Announcement i : annoList){
            AnnouncementDto annoDto = new AnnouncementDto(i.getTitle(), i.getContent(), i.getModifiedDate().toLocalDate());
            annoDtoList.add(annoDto);
        }
        return annoDtoList;
    }
    @Transactional
    public List<TermDto> getTermList(){
        List<Term> termList = termRepository.findAll();
        List<TermDto> termDtoList = new ArrayList<>();
        for(Term i : termList){
            TermDto termDto = new TermDto(i.getId(), i.getName(), i.getDetail());
            termDtoList.add(termDto);
        }
        return termDtoList;
    }

    @Transactional
    public List<ReviewDto> getTrainerReviewList(Long trainerIdx){
        Optional<Trainer> optional = trainerRepository.findById(trainerIdx);
        if(optional.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_TRAINERIDX);
        }
        List<Review> reviewList = optional.get().getReviewList();
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        for(Review i : reviewList){
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setName(i.getCustomer().getName());
            reviewDto.setProfile(i.getCustomer().getProfile());
            reviewDto.setGrade(i.getGrade());
            reviewDto.setContents(i.getContent());
            reviewDto.setCreatedAt(i.getCreatedDate().toLocalDate());
            reviewDtoList.add(reviewDto);
        }
        Collections.reverse(reviewDtoList);
        return reviewDtoList;
    }

    @Transactional
    public TrainerInformationForUserDto getTrainerInformation(Long trainerIdx){
        Optional<Trainer> optional = trainerRepository.findById(trainerIdx);
        if(optional.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_TRAINERIDX);
        }
        Optional<UserImg> userImg = userImgRepository.findByTrainer(optional.get());
        TrainerInformationForUserDto trainerInfo = new TrainerInformationForUserDto();
        trainerInfo.setName(optional.get().getUser().getName());
        trainerInfo.setProfile(userImg.get().getProfile());
        trainerInfo.setBackground(userImg.get().getBackGround());
        trainerInfo.setLevelName(optional.get().getLevel().getName());
        trainerInfo.setSchool(optional.get().getSchool());
        trainerInfo.setGrade(optional.get().getGrade());
        trainerInfo.setCost(String.valueOf(optional.get().getPriceHour()));
        trainerInfo.setIntro(optional.get().getIntro());
        trainerInfo.setService(optional.get().getService());
        List<ReviewDto> reviewList = getTrainerReviewList(trainerIdx);
        trainerInfo.setReviewDto(reviewList);
        List<EtcImg> etcImgList = userImg.get().getEtcImgList();
        List<String> imageList = new ArrayList<>();
        for(EtcImg i : etcImgList){
            imageList.add(i.getEtcImg());
        }
        trainerInfo.setImageList(imageList);
        trainerInfo.setState(optional.get().getUser().getUserState());
        return trainerInfo;
    }

    @Transactional
    public MyPageDto getMyPageBriefInformation() throws BusinessException{
        Optional<Account> optional = accountRepository.findById(SecurityUtil.getLoginUserId());
        if(optional.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }
        MyPageDto myPageDto = new MyPageDto();
        if(optional.get().getAccountType().equals(AccountType.TRAINER)){
            Optional<Trainer> optionalT = trainerRepository.findById(optional.get().getId());
            if(optionalT.isEmpty()){
                throw new BusinessException(ErrorCode.INVALID_TRAINERIDX);
            }
            myPageDto.setProfile(optionalT.get().getUserImg().getProfile());
        }
        else{
            myPageDto.setProfile(optional.get().getProfile());
        }
        myPageDto.setUserIdx(optional.get().getId());
        myPageDto.setUserName(optional.get().getName());
        myPageDto.setEmail(optional.get().getEmail());
        myPageDto.setLocation(optional.get().getLocation());
        return myPageDto;
    }

    public String convertCategoryForClient(Category category){
        if (category.equals(Category.PERSONAL_PT)){
            return "pt";
        } else if(category.equals(Category.DIET)){
            return "diet";
        } else if(category.equals(Category.FOOD_CHECK)){
            return "food";
        } else if(category.equals(Category.REHAB)){
            return "rehab";
        } else if(category.equals(Category.FIT_MATE)){
            return "friend";
        }
        return null;
    }

    @Transactional
    public String getAllTermContents(){
        Optional<Account> optional = accountRepository.findById(SecurityUtil.getLoginUserId());
        if(optional.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }
        List<TermAgree> termList = termAgreeRepository.findAllByUser(optional.get());
        String terms = "";
        int count = 1;
        for(TermAgree i : termList){
            terms += Integer.toString(count) + "." + i.getTerm().getName() + ":" + i.getTerm().getDetail() + " ";
        }
        return terms;
    }

    @Transactional
    public void modifyUserLocation(String location) throws BusinessException{
        Optional<Account> optional = accountRepository.findById(SecurityUtil.getLoginUserId());
        if(optional.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }
        optional.get().modifyLocation(location);
        try{
            accountRepository.save(optional.get());
        } catch (Exception e){
            throw new BusinessException(ErrorCode.DB_MODIFY_ERROR);
        }
    }
    @Transactional
    public String getUserState(Long userIdx) throws BusinessException{
        Optional<Account> optional = accountRepository.findById(userIdx);
        if(optional.isEmpty()){
            throw new BusinessException(ACCOUNT_NOT_FOUND);
        }
        return optional.get().getUserState();
    }
}