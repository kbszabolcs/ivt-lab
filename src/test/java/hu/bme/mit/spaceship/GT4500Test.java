package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;

  private TorpedoStore mockPTS;
  private TorpedoStore mockSTS;

  @BeforeEach
  public void init(){
    mockPTS = mock(TorpedoStore.class);
    mockSTS = mock(TorpedoStore.class);
    this.ship = new GT4500(mockPTS,mockSTS);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockPTS.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    verify(mockPTS, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mockPTS.fire(1)).thenReturn(true);
    when(mockSTS.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);

    verify(mockPTS, times(1)).fire(1);
    verify(mockSTS, times(1)).fire(1);
  }

  // 3.

  @Test
  public void fireTorpedo_firesecondaryagainwhenprimaryisempty(){
    // Arrange
    when(mockPTS.isEmpty()).thenReturn(true);
    when(mockSTS.fire(1)).thenReturn(true);

    //create inOrder object passing any mocks that need to be verified in order
    InOrder inOrder = inOrder(mockPTS, mockSTS);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE); //secondary
    ship.fireTorpedo(FiringMode.SINGLE); //secondary
    
    //Assert
    inOrder.verify(mockSTS).fire(1);
    inOrder.verify(mockSTS).fire(1);
    
  }

  @Test
  public void fireTorpedo_dontfireotherwhenoneisfail(){
    // Arrange
    when(mockSTS.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    verify(mockSTS, never()).fire(1);
  }

  @Test
  public void fireTorpedo_fireotherwhenoneisempty(){
    // Arrange
    when(mockPTS.isEmpty()).thenReturn(true);
    when(mockSTS.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    verify(mockSTS).fire(1);
  }

  @Test
  public void fireTorpedo_ALL_rotatetorpedostores(){
    // Arrange
    when(mockPTS.fire(1)).thenReturn(true);
    when(mockSTS.fire(1)).thenReturn(true);

    //create inOrder object passing any mocks that need to be verified in order
    InOrder inOrder = inOrder(mockPTS, mockSTS);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE); //primary
    ship.fireTorpedo(FiringMode.SINGLE); //secondary
    ship.fireTorpedo(FiringMode.SINGLE); //primary
    ship.fireTorpedo(FiringMode.SINGLE); //secondary

    //following will make sure that firstMock was called before secondMock
    inOrder.verify(mockPTS).fire(1);
    inOrder.verify(mockSTS).fire(1);
    inOrder.verify(mockPTS).fire(1);
    inOrder.verify(mockSTS).fire(1);
  }

  @Test
  public void fireTorpedo_ALL_PrimaryStore_First(){
    // Arrange
    when(mockPTS.fire(1)).thenReturn(true);
    when(mockSTS.fire(1)).thenReturn(true);

    //create inOrder object passing any mocks that need to be verified in order
    InOrder inOrder = inOrder(mockPTS, mockSTS);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);
    
    // Assert
    assertEquals(true, result);

    //following will make sure that firstMock was called before secondMock
    inOrder.verify(mockPTS).fire(1);
    inOrder.verify(mockSTS).fire(1);
  }

  @Test
  public void fireTorpedo_SINGLE_onlyonefires(){
    // Arrange
    when(mockPTS.fire(1)).thenReturn(true);
    when(mockSTS.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    
    // Assert
    assertEquals(true, result);

    //using mocks - mock is interacted
    verify(mockPTS).fire(1);

    //verify that method was never called on a mock
    verify(mockSTS, never()).fire(1);
  }




  // 100% coverage on fireTorpedo
  
  @Test
  public void fireTorpedo_fireprimaryagainwhensecondaryisempty(){
    // Arrange
    when(mockSTS.isEmpty()).thenReturn(true);
    when(mockPTS.fire(1)).thenReturn(true);

    //create inOrder object passing any mocks that need to be verified in order
    InOrder inOrder = inOrder(mockPTS, mockSTS);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE); //primary
    ship.fireTorpedo(FiringMode.SINGLE); //primary
    
    //Assert
    inOrder.verify(mockPTS).fire(1);
    inOrder.verify(mockPTS).fire(1);
    
  }

}
