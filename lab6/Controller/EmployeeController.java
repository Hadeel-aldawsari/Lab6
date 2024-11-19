package com.example.lab6.Controller;


import com.example.lab6.ApiResponse.ApiResponse;
import com.example.lab6.Model.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    ArrayList<Employee>employees=new ArrayList<>();

    @GetMapping("/get-all")
    public ResponseEntity getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid Employee employee, Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getFieldError().getDefaultMessage());
        }
employees.add(employee);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee add successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity update(@PathVariable String id,@RequestBody @Valid Employee employee,Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getFieldError().getDefaultMessage());
        }
        int index=0;
        for(Employee e:employees){
            if(e.getID().equals(id)){
                index=employees.indexOf(e);
                employees.set(index,employee);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("employee updated successfully"));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Employee Not found"));
    }

    @DeleteMapping("/delete/{id}")
public ResponseEntity<ApiResponse> delete(@PathVariable String id){
        int index=-1;
        for(Employee employee:employees){
            if(employee.getID().equals(id)){
                index=employees.indexOf(employee);
                break;}
        }
       if(index==-1)return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("employee not found"));
               employees.remove(index);
       return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("employee updated successfully"));
    }


    @GetMapping("/search/{position}")
    public ResponseEntity search(@PathVariable String position){
       if(! (position.equalsIgnoreCase("coordinator")||position.equalsIgnoreCase("supervisor")))
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Position should be  supervisor or coordinator"));

        ArrayList<Employee>employeesByPosition=new ArrayList<>();
        for(Employee e:employees){
            if(e.getPosition().equalsIgnoreCase(position))
                employeesByPosition.add(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeesByPosition);
    }


@GetMapping("/employees-by-age/{min}/{max}")
public ResponseEntity employeesByAge(@PathVariable int min, @PathVariable int max){
       if(!(min>=0 && max>=0))return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Age :should be valid age values "));
   ArrayList<Employee>employeesByAge=new ArrayList<>();
   for (Employee employee:employees){
       if(employee.getAge()>=min && employee.getAge()<=max)
           employeesByAge.add(employee);
   }
   return ResponseEntity.status(HttpStatus.OK).body(employeesByAge);
}


@PutMapping("/apply-leave/{id}")
public ResponseEntity applyForLeave(@PathVariable String id){
        boolean exist=false;
        int index=-1;
        for(Employee e:employees){
          if(e.getID().equalsIgnoreCase(id))
              exist=true;
          index=employees.indexOf(e);
        }
        if(!exist)return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("employee not exist"));

        if(employees.get(index).isOnLeave())return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("employee already on leave"));
        if(employees.get(index).getAnnualLeave()<=0)return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("No annual leave remaining!!"));

        employees.get(index).setOnLeave(true);
        employees.get(index).setAnnualLeave( employees.get(index).getAnnualLeave()-1);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("annual leave applied successfully "));

}

@GetMapping("/get-employee-with-no-leaves")
public ResponseEntity EmployeeswithNoAnnualLeave(){
        ArrayList<Employee>withNoLeaves=new ArrayList<>();
        for (Employee employee:employees){
          if(employee.getAnnualLeave()==0){
              withNoLeaves.add(employee);
          }
        }
        return ResponseEntity.status(HttpStatus.OK).body(withNoLeaves);
}



@PutMapping("/Promote-employee/{requesterID}/{employeeID}")
public ResponseEntity PromoteEmployee(@PathVariable String requesterID,@PathVariable String employeeID){
//check requester
        for(Employee requester:employees ){
            if(requester.getID().equalsIgnoreCase(requesterID)){
              if(!requester.getPosition().equalsIgnoreCase("supervisor"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Sorry requester should be supervisor"));
            }
        }

        //check Employee exist && age && on leave
        boolean exist=false;
      int index=-1;
        for (Employee e:employees){
          if( e.getID().equalsIgnoreCase(employeeID)) {
              exist = true;
              index = employees.indexOf(e);
          }
        }
        if(!exist)return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("employee with this id not exist"));
        if( employees.get(index).getAge()<30)return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("employee's age should be at least 30 years. "));
        if(employees.get(index).isOnLeave())return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Sorry the employee currently on leave."));

        //meet the criteria
         employees.get(index).setPosition("supervisor");
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("employee's position to supervisor successfully"));





}








}
