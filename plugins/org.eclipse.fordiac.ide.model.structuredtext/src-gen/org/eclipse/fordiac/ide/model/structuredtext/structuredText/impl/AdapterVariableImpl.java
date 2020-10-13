/**
 * generated by Xtext 2.22.0
 */
package org.eclipse.fordiac.ide.model.structuredtext.structuredText.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration;

import org.eclipse.fordiac.ide.model.structuredtext.structuredText.AdapterVariable;
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Adapter Variable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.impl.AdapterVariableImpl#getCurr <em>Curr</em>}</li>
 *   <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.impl.AdapterVariableImpl#getVar <em>Var</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AdapterVariableImpl extends VariableImpl implements AdapterVariable
{
  /**
   * The cached value of the '{@link #getCurr() <em>Curr</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCurr()
   * @generated
   * @ordered
   */
  protected AdapterVariable curr;

  /**
   * The cached value of the '{@link #getVar() <em>Var</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVar()
   * @generated
   * @ordered
   */
  protected VarDeclaration var;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AdapterVariableImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return StructuredTextPackage.Literals.ADAPTER_VARIABLE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public AdapterVariable getCurr()
  {
    return curr;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetCurr(AdapterVariable newCurr, NotificationChain msgs)
  {
    AdapterVariable oldCurr = curr;
    curr = newCurr;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StructuredTextPackage.ADAPTER_VARIABLE__CURR, oldCurr, newCurr);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setCurr(AdapterVariable newCurr)
  {
    if (newCurr != curr)
    {
      NotificationChain msgs = null;
      if (curr != null)
        msgs = ((InternalEObject)curr).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StructuredTextPackage.ADAPTER_VARIABLE__CURR, null, msgs);
      if (newCurr != null)
        msgs = ((InternalEObject)newCurr).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StructuredTextPackage.ADAPTER_VARIABLE__CURR, null, msgs);
      msgs = basicSetCurr(newCurr, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StructuredTextPackage.ADAPTER_VARIABLE__CURR, newCurr, newCurr));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public VarDeclaration getVar()
  {
    if (var != null && var.eIsProxy())
    {
      InternalEObject oldVar = (InternalEObject)var;
      var = (VarDeclaration)eResolveProxy(oldVar);
      if (var != oldVar)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, StructuredTextPackage.ADAPTER_VARIABLE__VAR, oldVar, var));
      }
    }
    return var;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public VarDeclaration basicGetVar()
  {
    return var;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setVar(VarDeclaration newVar)
  {
    VarDeclaration oldVar = var;
    var = newVar;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StructuredTextPackage.ADAPTER_VARIABLE__VAR, oldVar, var));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case StructuredTextPackage.ADAPTER_VARIABLE__CURR:
        return basicSetCurr(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case StructuredTextPackage.ADAPTER_VARIABLE__CURR:
        return getCurr();
      case StructuredTextPackage.ADAPTER_VARIABLE__VAR:
        if (resolve) return getVar();
        return basicGetVar();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case StructuredTextPackage.ADAPTER_VARIABLE__CURR:
        setCurr((AdapterVariable)newValue);
        return;
      case StructuredTextPackage.ADAPTER_VARIABLE__VAR:
        setVar((VarDeclaration)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case StructuredTextPackage.ADAPTER_VARIABLE__CURR:
        setCurr((AdapterVariable)null);
        return;
      case StructuredTextPackage.ADAPTER_VARIABLE__VAR:
        setVar((VarDeclaration)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case StructuredTextPackage.ADAPTER_VARIABLE__CURR:
        return curr != null;
      case StructuredTextPackage.ADAPTER_VARIABLE__VAR:
        return var != null;
    }
    return super.eIsSet(featureID);
  }

} //AdapterVariableImpl
