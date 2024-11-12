package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.ServicePackageDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.ServicePackage;
import com.hotel.hotel_stars.Models.ServicePackageModel;
import com.hotel.hotel_stars.Repository.ServicePackageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePackageService {
    @Autowired
    ServicePackageRepository servicePackageRepository;

    @Autowired
    ModelMapper modelMapper;

    public ServicePackageDto convertToDto(ServicePackage servicePackage) {
        ServicePackageDto servicePackageDto = modelMapper.map(servicePackage, ServicePackageDto.class);
        return servicePackageDto;
    }

    public List<ServicePackageDto> getAllServicePageke() {
        List<ServicePackage> servicePackages = servicePackageRepository.findAll();
        return servicePackages.stream().map(this::convertToDto).toList();
    }

    public ServicePackageDto findById(Integer id) {
        ServicePackage servicePackage = servicePackageRepository.findById(id).get();
        return convertToDto(servicePackage);
    }

    public StatusResponseDto addServicePackage(ServicePackageModel servicePackageModel) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();
        try {
            ServicePackage servicePackage = new ServicePackage();
            servicePackage.setServicePackageName(servicePackageModel.getServicePackageName());
            servicePackage.setPrice(servicePackageModel.getPrice());
            servicePackageRepository.save(servicePackage);

            statusResponseDto.setCode("200");
            statusResponseDto.setMessage("Gói dịch vụ đã được thêm thành công");
            statusResponseDto.setStatus("success");
        } catch (DataIntegrityViolationException e) {
            statusResponseDto.setCode("400");
            statusResponseDto.setMessage("Lỗi vi phạm dữ liệu: " + e.getMessage());
            statusResponseDto.setStatus("error");
        } catch (Exception e) {
            statusResponseDto.setCode("500");
            statusResponseDto.setMessage("Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            statusResponseDto.setStatus("error");
        }
        return statusResponseDto;
    }

    public StatusResponseDto updateServicePackage(ServicePackageModel servicePackageModel) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();
        try {
            ServicePackage servicePackage = servicePackageRepository.findById(servicePackageModel.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy gói dịch vụ"));

            servicePackage.setServicePackageName(servicePackageModel.getServicePackageName());
            servicePackage.setPrice(servicePackageModel.getPrice());
            servicePackageRepository.save(servicePackage);

            statusResponseDto.setCode("200");
            statusResponseDto.setMessage("Gói dịch vụ đã được cập nhật thành công");
            statusResponseDto.setStatus("success");
        } catch (EntityNotFoundException e) {
            statusResponseDto.setCode("404");
            statusResponseDto.setMessage(e.getMessage());
            statusResponseDto.setStatus("error");
        } catch (DataIntegrityViolationException e) {
            statusResponseDto.setCode("400");
            statusResponseDto.setMessage("Lỗi vi phạm dữ liệu: " + e.getMessage());
            statusResponseDto.setStatus("error");
        } catch (Exception e) {
            statusResponseDto.setCode("500");
            statusResponseDto.setMessage("Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            statusResponseDto.setStatus("error");
        }
        return statusResponseDto;
    }
    
    public StatusResponseDto deleteServicePackage(Integer id) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();
        try {
            ServicePackage servicePackage = servicePackageRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy gói dịch vụ"));
            servicePackageRepository.deleteById(id);

            statusResponseDto.setCode("200");
            statusResponseDto.setMessage("Gói dịch vụ đã được cập nhật thành công");
            statusResponseDto.setStatus("success");
        } catch (EntityNotFoundException e) {
            statusResponseDto.setCode("404");
            statusResponseDto.setMessage(e.getMessage());
            statusResponseDto.setStatus("error");
        } catch (DataIntegrityViolationException e) {
            statusResponseDto.setCode("400");
            statusResponseDto.setMessage("Không thể xóa gói dịch vụ");
            statusResponseDto.setStatus("error");
        } catch (Exception e) {
            statusResponseDto.setCode("500");
            statusResponseDto.setMessage("Không thể xóa gói dịch vụ");
            statusResponseDto.setStatus("error");
        }
        return statusResponseDto;
    }

}
