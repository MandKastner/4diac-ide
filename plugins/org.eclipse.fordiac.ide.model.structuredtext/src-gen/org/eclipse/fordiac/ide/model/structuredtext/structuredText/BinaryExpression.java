/**
 * generated by Xtext 2.22.0
 */
package org.eclipse.fordiac.ide.model.structuredtext.structuredText;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Binary Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.BinaryExpression#getLeft <em>Left</em>}</li>
 *   <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.BinaryExpression#getOperator <em>Operator</em>}</li>
 *   <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.BinaryExpression#getRight <em>Right</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage#getBinaryExpression()
 * @model
 * @generated
 */
public interface BinaryExpression extends Expression
{
  /**
   * Returns the value of the '<em><b>Left</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Left</em>' containment reference.
   * @see #setLeft(Expression)
   * @see org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage#getBinaryExpression_Left()
   * @model containment="true"
   * @generated
   */
  Expression getLeft();

  /**
   * Sets the value of the '{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.BinaryExpression#getLeft <em>Left</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Left</em>' containment reference.
   * @see #getLeft()
   * @generated
   */
  void setLeft(Expression value);

  /**
   * Returns the value of the '<em><b>Operator</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.BinaryOperator}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operator</em>' attribute.
   * @see org.eclipse.fordiac.ide.model.structuredtext.structuredText.BinaryOperator
   * @see #setOperator(BinaryOperator)
   * @see org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage#getBinaryExpression_Operator()
   * @model
   * @generated
   */
  BinaryOperator getOperator();

  /**
   * Sets the value of the '{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.BinaryExpression#getOperator <em>Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Operator</em>' attribute.
   * @see org.eclipse.fordiac.ide.model.structuredtext.structuredText.BinaryOperator
   * @see #getOperator()
   * @generated
   */
  void setOperator(BinaryOperator value);

  /**
   * Returns the value of the '<em><b>Right</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Right</em>' containment reference.
   * @see #setRight(Expression)
   * @see org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage#getBinaryExpression_Right()
   * @model containment="true"
   * @generated
   */
  Expression getRight();

  /**
   * Sets the value of the '{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.BinaryExpression#getRight <em>Right</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Right</em>' containment reference.
   * @see #getRight()
   * @generated
   */
  void setRight(Expression value);

} // BinaryExpression
